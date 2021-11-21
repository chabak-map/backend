package com.sikhye.chabak.src.member;

import com.sikhye.chabak.base.BaseException;
import com.sikhye.chabak.src.member.dto.JoinReq;
import com.sikhye.chabak.src.member.dto.LoginReq;
import com.sikhye.chabak.src.member.dto.LoginRes;
import com.sikhye.chabak.src.member.dto.MemberDto;
import com.sikhye.chabak.src.member.entity.Member;
import com.sikhye.chabak.utils.AES128;
import com.sikhye.chabak.utils.JwtService;
import com.sikhye.chabak.utils.sms.SmsService;
import com.sikhye.chabak.utils.sms.entity.SmsCacheKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.sikhye.chabak.base.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final RedisTemplate<String, String> redisTemplate;
	private final SmsService smsService;
	private final JwtService jwtService;


	@Value("{secret.USER_INFO_PASSWORD_KEY")
	private String USER_INFO_PASSWORD_KEY;

	@Override
	public LoginRes login(LoginReq loginReq) throws BaseException {
		String email = loginReq.getEmail();
		String password = loginReq.getPassword();

		// 입력된 이메일 기준 패스워드 찾기
		Member findMember = memberRepository.findMemberByEmail(email);
		String findPassword;
		try {
			findPassword = new AES128(USER_INFO_PASSWORD_KEY).decrypt(findMember.getPassword());
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new BaseException(PASSWORD_DECRYPTION_ERROR);
		}

		// 현재 찾은 비밀번호와 유저 입력 패스워드 비교
		if (password.equals(findPassword)) {
			// 유저 ID와 JWT를 만들어서 반환
			Long memberId = findMember.getId();
			String jwt = jwtService.createJwt(memberId);
			return new LoginRes(memberId, jwt);
		} else {
			throw new BaseException(FAILED_TO_LOGIN);
		}

	}

	@Override
	@Transactional
	public LoginRes join(JoinReq joinReq) throws BaseException {

		String encryptedPassword, encryptedPhoneNumber;

		try {
			encryptedPassword = new AES128(USER_INFO_PASSWORD_KEY).encrypt(joinReq.getPassword());
			encryptedPhoneNumber = new AES128(USER_INFO_PASSWORD_KEY).encrypt(joinReq.getPhoneNumber());
		} catch (Exception ignored) {
			throw new BaseException(PASSWORD_ENCRYPTION_ERROR);    // TODO: ENCRYPTION_ERROR 로 변경
		}

		try {
			// 새로운 멤버 저장
			Member newMember = Member.builder()
				.email(joinReq.getEmail())
				.nickname(joinReq.getNickname())
				.password(encryptedPassword)
				.phoneNumber(encryptedPhoneNumber)
				.build();

			Member savedMember = memberRepository.save(newMember);

			// JWT 토큰 생성
			String jwt = jwtService.createJwt(savedMember.getId());
			return new LoginRes(savedMember.getId(), jwt);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new BaseException(DATABASE_ERROR);    // TODO: 회원정보 저장 실패
		}


	}

	@Override
	public MemberDto lookup() throws BaseException {

		Long memberId = jwtService.getUserIdx();
		Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(DATABASE_ERROR));

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
			throw new BaseException(DATABASE_ERROR);    // TODO: 오류메시지 수정
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
			log.info("key = {}", key);
			log.info("--findCode = {}, verifyCode = {}", findCode, verifyCode);
			throw new BaseException(DATABASE_ERROR);  // TODO: 오류메시지 수정
		}

		return true;
	}

	// ================================================
	// INTERNAL USE
	// ================================================
	private String genRandomNum() {
		int maxNumLen = 6;

		Random random = new Random(System.currentTimeMillis());    // 중복방지 랜덤

		int range = (int) Math.pow(10, maxNumLen);
		int trim = (int) Math.pow(10, maxNumLen - 1);
		int result = random.nextInt(range) + trim;

		if (result > range) {
			result = result - trim;
		}

		log.info("generated Number = {}", result);
		return String.valueOf(result);

	}
}
