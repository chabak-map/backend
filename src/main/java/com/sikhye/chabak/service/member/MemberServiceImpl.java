package com.sikhye.chabak.service.member;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static com.sikhye.chabak.service.member.constant.BaseRole.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.image.UploadService;
import com.sikhye.chabak.service.jwt.JwtTokenService;
import com.sikhye.chabak.service.member.domain.Member;
import com.sikhye.chabak.service.member.domain.MemberRepository;
import com.sikhye.chabak.service.member.dto.EditMemberReq;
import com.sikhye.chabak.service.member.dto.JoinReq;
import com.sikhye.chabak.service.member.dto.LoginReq;
import com.sikhye.chabak.service.member.dto.LoginRes;
import com.sikhye.chabak.service.member.dto.MemberDto;
import com.sikhye.chabak.service.member.dto.PasswordReq;
import com.sikhye.chabak.service.oauth.constant.OAuthType;
import com.sikhye.chabak.service.sms.SmsService;
import com.sikhye.chabak.service.sms.entity.SmsCacheKey;
import com.sikhye.chabak.utils.encrypt.EncryptService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final RedisTemplate<String, String> redisTemplate;
	private final SmsService smsService;
	private final UploadService s3UploadService;
	private final EncryptService encryptService;
	private final JwtTokenService jwtTokenService;

	@PersistenceContext
	private EntityManager em;

	public MemberServiceImpl(MemberRepository memberRepository,
		RedisTemplate<String, String> redisTemplate, SmsService smsService,
		UploadService s3UploadService, EncryptService encryptService,
		JwtTokenService jwtTokenService) {
		this.memberRepository = memberRepository;
		this.redisTemplate = redisTemplate;
		this.smsService = smsService;
		this.s3UploadService = s3UploadService;
		this.encryptService = encryptService;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public LoginRes login(LoginReq loginReq) {
		String email = loginReq.getEmail();
		String password = loginReq.getPassword();

		// ????????? ????????? ?????? ???????????? ??????
		Member findMember = memberRepository.findMemberByEmailAndStatus(email, USED)
			.orElseThrow(() -> new BaseException(FAILED_TO_LOGIN));

		String findPassword;
		try {
			findPassword = encryptService.decrypt(findMember.getPassword());

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new BaseException(DECRYPTION_ERROR);
		}

		log.info("password = {}", findPassword);
		// ?????? ?????? ??????????????? ?????? ?????? ???????????? ??????
		if (password.equals(findPassword)) {
			Long memberId = findMember.getId();
			String jwt = jwtTokenService.createJwt(memberId, findMember.getRole());
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
			encryptedPassword = encryptService.encrypt(joinReq.getPassword());
		} catch (Exception ignored) {
			throw new BaseException(ENCRYPTION_ERROR);
		}

		// ?????????, ?????????, ???????????? ????????????
		Optional<Member> findMemberEmail = memberRepository.findMemberByEmailAndStatus(joinReq.getEmail(), USED);
		if (findMemberEmail.isPresent()) {
			throw new BaseException(POST_USERS_EXISTS_EMAIL);
		}

		Optional<Member> findMemberNickname = memberRepository.findMemberByNicknameAndStatus(joinReq.getNickname(),
			USED);
		if (findMemberNickname.isPresent()) {
			throw new BaseException(POST_USERS_EXISTS_NICKNAME);
		}

		Optional<Member> findMemberPhoneNumber = memberRepository.findMemberByPhoneNumberAndStatus(
			joinReq.getPhoneNumber(),
			USED);
		if (findMemberPhoneNumber.isPresent()) {
			throw new BaseException(POST_USERS_EXISTS_PHONE_NUMBER);
		}

		Member newMember = Member.builder()
			.email(joinReq.getEmail())
			.nickname(joinReq.getNickname())
			.password(encryptedPassword)
			.phoneNumber(joinReq.getPhoneNumber())
			.build();

		Member savedMember = memberRepository.save(newMember);
		em.refresh(savedMember);

		// JWT ?????? ??????
		String jwt = jwtTokenService.createJwt(savedMember.getId(), savedMember.getRole());
		return new LoginRes(savedMember.getId(), jwt);
	}

	@Override
	public MemberDto lookup() {

		Long memberId = jwtTokenService.getMemberId();
		Member findMember = memberRepository.findMemberByIdAndStatus(memberId, USED)
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

		String authMessage = "[????????????] ?????? ?????? [" + verifyCode + "]??? ??????????????????.";

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

		log.info("userInput Code = {}", verifyCode);
		log.info("inRedis Code = {}", findCode);

		if (!verifyCode.equals(findCode)) {
			throw new BaseException(INVALID_VERIFY_CODE);
		}

		return true;
	}

	@Override
	@Transactional
	public String uploadImage(MultipartFile memberImage) {

		Long memberId = jwtTokenService.getMemberId();

		// ????????? ??????
		String imageUrl = s3UploadService.uploadImage(memberImage, "images/member/");

		// ????????? URL ??????
		Member findMember = memberRepository.findMemberByIdAndStatus(memberId, USED)
			.orElseThrow(() -> new BaseException(CHECK_USER));

		findMember.setImageUrl(imageUrl);

		return findMember.getImageUrl();
	}

	@Override
	@Transactional
	public Long editMemberInform(EditMemberReq editMemberReq) {
		// ????????? ????????????
		Boolean duplicatedNickname = isDuplicatedNickname(editMemberReq.getNickname());

		System.out.println("duplicatedNickname = " + duplicatedNickname);

		if (duplicatedNickname) {
			throw new BaseException(POST_USERS_EXISTS_NICKNAME);
		}

		// ?????? ?????? ??????
		Long memberId = jwtTokenService.getMemberId();
		Member findMember = memberRepository.findMemberByIdAndStatus(memberId, USED)
			.orElseThrow(() -> new BaseException(CHECK_USER));

		// ???????????? ????????? ?????? ????????? ??????
		if (editMemberReq.getImage() != null) {
			String imageUrl = s3UploadService.uploadImage(editMemberReq.getImage(), "images/member/");
			findMember.editMemberInfo(editMemberReq.getNickname(), imageUrl);
		} else {
			findMember.editMemberNickname(editMemberReq.getNickname());
		}

		return findMember.getId();
	}

	@Override
	@Transactional
	public Long editPassword(PasswordReq passwordReq) {

		String encryptedPassword;
		try {
			encryptedPassword = encryptService.encrypt(passwordReq.getPassword());
		} catch (Exception exception) {
			throw new BaseException(ENCRYPTION_ERROR);
		}

		memberRepository.findMemberByIdAndStatus(passwordReq.getMemberId(), USED)
			.orElseThrow(() -> new BaseException(CHECK_USER)).setPassword(encryptedPassword);

		return passwordReq.getMemberId();

	}

	@Override
	public String findMemberEmail(String phoneNumber) {
		return memberRepository.findMemberByPhoneNumberAndStatus(phoneNumber, USED)
			.orElseThrow(() -> new BaseException(CHECK_USER)).getEmail();
	}

	@Override
	public Long findMember(String phoneNumber, String email) {
		return memberRepository.findMemberByPhoneNumberAndEmailAndStatus(phoneNumber, email, USED)
			.orElseThrow(() -> new BaseException(CHECK_USER)).getId();
	}

	@Override
	@Transactional
	public Long statusToDeleteMember() {
		Long memberId = jwtTokenService.getMemberId();

		memberRepository.findMemberByIdAndStatus(memberId, USED)
			.orElseThrow(() -> new BaseException(NOT_TO_DELETE)).setStatusToDelete();

		return memberId;
	}

	@Override
	public Boolean isDuplicatedNickname(String nickname) {
		return memberRepository.existsByNicknameAndStatus(nickname, USED);
	}

	@Override
	public Boolean isDuplicatedEmail(String email) {
		return memberRepository.existsByEmailAndStatus(email, USED);
	}

	@Override
	public Optional<Member> findMemberBy(String email) {
		return memberRepository.findMemberByEmailAndStatus(email, USED);
	}

	@Override
	public Optional<Member> findMemberBy(OAuthType socialType, String socialId) {
		return memberRepository.findMemberBySocialTypeAndSocialIdAndStatus(socialType, socialId, USED);
	}

	@Override
	public Optional<Member> findMemberBy(Long memberId) {
		return memberRepository.findMemberByIdAndStatus(memberId, USED);
	}

	@Override
	public Optional<List<Member>> findAllAdmin() {
		return memberRepository.findMembersByRoleAndStatus(ROLE_ADMIN, USED);
	}

	// ================================================
	// INTERNAL USE
	// ================================================
	private String genRandomNum() {
		int maxNumLen = 6;

		Random random = new Random(System.currentTimeMillis());    // ???????????? ??????

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
