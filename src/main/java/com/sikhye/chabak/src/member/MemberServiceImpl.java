package com.sikhye.chabak.src.member;

import com.sikhye.chabak.base.BaseException;
import com.sikhye.chabak.src.member.dto.JoinReq;
import com.sikhye.chabak.src.member.dto.LoginReq;
import com.sikhye.chabak.src.member.dto.LoginRes;
import com.sikhye.chabak.src.member.dto.MemberDto;
import com.sikhye.chabak.src.member.entity.Member;
import com.sikhye.chabak.utils.AES128;
import com.sikhye.chabak.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sikhye.chabak.base.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final JwtService jwtService;
	private final MemberRepository memberRepository;

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
			throw new BaseException(PASSWORD_ENCRYPTION_ERROR);	// TODO: ENCRYPTION_ERROR 로 변경
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
			throw new BaseException(DATABASE_ERROR);	// TODO: 회원정보 저장 실패
		}


	}

	@Override
	public MemberDto lookup(Long memberId) throws BaseException {

		Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(DATABASE_ERROR));

		return MemberDto.builder()
			.id(findMember.getId())
			.imageUrl(findMember.getImageUrl())
			.nickname(findMember.getNickname())
			.build();
	}
}
