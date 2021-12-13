package com.sikhye.chabak.service.oauth;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.service.member.MemberService;
import com.sikhye.chabak.service.member.dto.JoinReq;
import com.sikhye.chabak.service.member.dto.LoginReq;
import com.sikhye.chabak.service.member.dto.LoginRes;
import com.sikhye.chabak.service.member.entity.Member;
import com.sikhye.chabak.service.oauth.constant.OAuthType;
import com.sikhye.chabak.service.oauth.dto.UserInfo;
import com.sikhye.chabak.service.oauth.social.SocialOAuth;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
public class OAuthService {
	private final List<SocialOAuth> socialOauthList;
	private final HttpServletResponse response;
	private final MemberService memberService;

	public OAuthService(List<SocialOAuth> socialOauthList, HttpServletResponse response, MemberService memberService) {
		this.socialOauthList = socialOauthList;
		this.response = response;
		this.memberService = memberService;
	}

	public void requestConnect(OAuthType socialType) {
		SocialOAuth socialOAuth = this.findSocialOAuthByType(socialType);
		String redirectURL = socialOAuth.getOAuthRedirectUrl();
		try {
			response.sendRedirect(redirectURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public LoginRes socialLogin(OAuthType socialType, String code) throws Exception {

		// 소셜 정보
		SocialOAuth socialOAuth = this.findSocialOAuthByType(socialType);

		UserInfo userInfo = socialOAuth.getUserInfo(code);

		String socialId = userInfo.getId();
		String nickname = userInfo.getNickname();
		String email = userInfo.getEmail();

		log.info("socialId = {}, nickname = {}, email = {}", socialId, nickname, email);

		Member member = new Member();
		Optional<Member> socialIdMember = memberService.findMemberBy(socialType, socialId);

		// Case 1. 소셜 ID 유저 존재
		if (socialIdMember.isPresent()) {
			member = socialIdMember.get();
		} else {
			// Case 2. 소셜 이메일 유저 존재
			Optional<Member> emailMember = memberService.findMemberBy(email);
			if (emailMember.isPresent()) {
				emailMember.get().setSocialInfo(socialType, socialId);
				member = emailMember.get();
			} else {
				// Case 3. 미가입 유저 -> 소셜 회원가입
				JoinReq joinReq = JoinReq.builder()
					.email(email)
					.nickname(socialId + "_" + nickname)
					.password(socialId + socialOAuth.getInitPassword())
					.build();

				memberService.join(joinReq);
				log.info("회원가입 완료");
			}
		}

		// 회원가입 후 로그인
		LoginReq loginReq = LoginReq.builder()
			.email(email)
			.password(socialId + socialOAuth.getInitPassword())
			.build();

		log.info("로그인 시도");
		log.info("member.getEmail() = " + member.getEmail());
		log.info("socialOAuth.getInitPassword() = " + socialOAuth.getInitPassword());
		return memberService.login(loginReq);
	}

	// ======================================
	// INTERNAL USE
	// ======================================
	private SocialOAuth findSocialOAuthByType(OAuthType socialLoginType) {
		return socialOauthList.stream()
			.filter(x -> x.type() == socialLoginType)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
	}

}
