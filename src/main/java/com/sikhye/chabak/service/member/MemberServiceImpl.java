package com.sikhye.chabak.service.member;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static com.sikhye.chabak.global.time.BaseStatus.*;
import static com.sikhye.chabak.service.member.constant.BaseRole.*;

import java.util.Optional;
import java.util.Random;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.image.UploadService;
import com.sikhye.chabak.service.jwt.JwtTokenService;
import com.sikhye.chabak.service.member.dto.EditMemberReq;
import com.sikhye.chabak.service.member.dto.JoinReq;
import com.sikhye.chabak.service.member.dto.LoginReq;
import com.sikhye.chabak.service.member.dto.LoginRes;
import com.sikhye.chabak.service.member.dto.MemberDto;
import com.sikhye.chabak.service.member.dto.PasswordReq;
import com.sikhye.chabak.service.member.entity.Member;
import com.sikhye.chabak.service.member.sms.SmsService;
import com.sikhye.chabak.service.member.sms.entity.SmsCacheKey;
import com.sikhye.chabak.utils.encrypt.AES256;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final RedisTemplate<String, String> redisTemplate;
	private final SmsService smsService;
	private final UploadService s3UploadService;
	private final AES256 aes256Service;
	private final JwtTokenService jwtTokenService;

	@Builder
	public MemberServiceImpl(MemberRepository memberRepository, RedisTemplate<String, String> redisTemplate,
		SmsService smsService, UploadService s3UploadService, AES256 aes256Service, JwtTokenService jwtTokenService) {
		this.memberRepository = memberRepository;
		this.redisTemplate = redisTemplate;
		this.smsService = smsService;
		this.s3UploadService = s3UploadService;
		this.aes256Service = aes256Service;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public LoginRes login(LoginReq loginReq) {
		String email = loginReq.getEmail();
		String password = loginReq.getPassword();

		// 입력된 이메일 기준 패스워드 찾기
		Member findMember = memberRepository.findMemberByEmailAndStatus(email, used)
			.orElseThrow(() -> new BaseException(FAILED_TO_LOGIN));

		String findPassword;
		try {
			findPassword = aes256Service.decrypt(findMember.getPassword());

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new BaseException(DECRYPTION_ERROR);
		}

		// 현재 찾은 비밀번호와 유저 입력 패스워드 비교
		if (password.equals(findPassword)) {
			Long memberId = findMember.getId();
			String jwt = jwtTokenService.createJwt(memberId, ROLE_USER);
			return new LoginRes(memberId, jwt);
		} else {
			throw new BaseException(FAILED_TO_LOGIN);
		}
	}

	@Override
	@Transactional
	public LoginRes join(JoinReq joinReq) {

		String encryptedPassword;

		try {
			encryptedPassword = aes256Service.encrypt(joinReq.getPassword());
		} catch (Exception ignored) {
			throw new BaseException(ENCRYPTION_ERROR);
		}

		// 닉네임, 이메일 중복체크
		Optional<Member> findMemberEmail = memberRepository.findMemberByEmailAndStatus(joinReq.getEmail(), used);
		if (findMemberEmail.isPresent()) {
			throw new BaseException(POST_USERS_EXISTS_EMAIL);
		}

		Optional<Member> findMemberNickname = memberRepository.findMemberByNicknameAndStatus(joinReq.getNickname(),
			used);
		if (findMemberNickname.isPresent()) {
			throw new BaseException(POST_USERS_EXISTS_NICKNAME);
		}

		Member newMember = Member.builder()
			.email(joinReq.getEmail())
			.nickname(joinReq.getNickname())
			.password(encryptedPassword)
			.phoneNumber(joinReq.getPhoneNumber())
			.build();

		Member savedMember = memberRepository.save(newMember);

		// JWT 토큰 생성
		String jwt = jwtTokenService.createJwt(savedMember.getId(), ROLE_USER);
		return new LoginRes(savedMember.getId(), jwt);
	}

	@Override
	public MemberDto lookup() {

		Long memberId = jwtTokenService.getMemberId();
		Member findMember = memberRepository.findMemberByIdAndStatus(memberId, used)
			.orElseThrow(() -> new BaseException(CHECK_USER));

		return MemberDto.builder()
			.id(findMember.getId())
			.imageUrl(findMember.getImageUrl())
			.nickname(findMember.getNickname())
			.build();
	}

	@Override
	@Transactional
	@CachePut(value = SmsCacheKey.SMS, key = "#phoneNumber")
	public String requestPhoneAuth(String phoneNumber) throws BaseException {

		String verifyCode = genRandomNum();
		String key = SmsCacheKey.SMS.concat("::").concat(phoneNumber);

		redisTemplate.opsForValue().set(key, verifyCode);

		String authMessage = "[ㅊㅂㅊㅂ] 인증 코드 [" + verifyCode + "]를 입력해주세요.";

		try {
			smsService.sendSms(phoneNumber, authMessage);
		} catch (Exception exception) {
			throw new BaseException(SMS_ERROR);
		}

		String findCode = redisTemplate.opsForValue().get(key);
		log.info("++findCode = {}, verifyCode = {}", findCode, verifyCode);

		return verifyCode;
	}

	@Override
	@CacheEvict(value = SmsCacheKey.SMS, key = "#phoneNumber")
	public Boolean verifySms(String verifyCode, String phoneNumber) throws BaseException {

		String key = SmsCacheKey.SMS.concat("::").concat(phoneNumber);

		String findCode = redisTemplate.opsForValue().get(key);

		if (!verifyCode.equals(findCode)) {
			throw new BaseException(INVALID_VERIFY_CODE);
		}

		return true;
	}

	@Override
	@Transactional
	public String uploadImage(MultipartFile memberImage) {

		Long memberId = jwtTokenService.getMemberId();

		// 이미지 저장
		String imageUrl = s3UploadService.uploadImage(memberImage, "images/member/");

		// 이미지 URL 저장
		Member findMember = memberRepository.findMemberByIdAndStatus(memberId, used)
			.orElseThrow(() -> new BaseException(CHECK_USER));

		findMember.setImageUrl(imageUrl);

		return findMember.getImageUrl();
	}

	@Override
	@Transactional
	public Long editMemberInform(EditMemberReq editMemberReq) {
		// 변경 가능: 닉네임, 프로필 이미지
		Long memberId = jwtTokenService.getMemberId();

		Member findMember = memberRepository.findMemberByIdAndStatus(memberId, used)
			.orElseThrow(() -> new BaseException(CHECK_USER));

		String imageUrl = s3UploadService.uploadImage(editMemberReq.getImage(), "images/member/");

		findMember.editMemberInfo(editMemberReq.getNickname(), imageUrl);

		return findMember.getId();
	}

	@Override
	@Transactional
	public Long editPassword(PasswordReq passwordReq) {

		String encryptedPassword;
		try {
			encryptedPassword = aes256Service.encrypt(passwordReq.getPassword());
		} catch (Exception exception) {
			throw new BaseException(ENCRYPTION_ERROR);
		}

		memberRepository.findMemberByIdAndStatus(passwordReq.getMemberId(), used)
			.orElseThrow(() -> new BaseException(CHECK_USER)).setPassword(encryptedPassword);

		return passwordReq.getMemberId();

	}

	@Override
	public String findMemberEmail(String phoneNumber) {
		return memberRepository.findMemberByPhoneNumberAndStatus(phoneNumber, used)
			.orElseThrow(() -> new BaseException(CHECK_USER)).getEmail();
	}

	@Override
	public Long findMember(String phoneNumber, String email) {
		return memberRepository.findMemberByPhoneNumberAndEmailAndStatus(phoneNumber, email, used)
			.orElseThrow(() -> new BaseException(CHECK_USER)).getId();
	}

	@Override
	@Transactional
	public Long statusToDeleteMember() {
		Long memberId = jwtTokenService.getMemberId();

		memberRepository.findMemberByIdAndStatus(memberId, used)
			.orElseThrow(() -> new BaseException(NOT_TO_DELETE)).setStatusToDelete();

		return memberId;
	}

	@Override
	public Boolean isDuplicatedNickname(String nickname) {
		return memberRepository.existsByNicknameAndStatus(nickname, used);
	}

	@Override
	public Boolean isDuplicatedEmail(String email) {
		return memberRepository.existsByEmailAndStatus(email, used);
	}

	// ================================================
	// INTERNAL USE
	// ================================================
	// ptpt: 중복방지 랜덤 생성
	private String genRandomNum() {
		int maxNumLen = 6;

		Random random = new Random(System.currentTimeMillis());    // 중복방지 랜덤

		int range = (int)Math.pow(10, maxNumLen);
		int trim = (int)Math.pow(10, maxNumLen - 1);
		int result = random.nextInt(range) + trim;

		if (result > range) {
			result = result - trim;
		}

		log.info("generated Number = {}", result);
		return String.valueOf(result);

	}
}
