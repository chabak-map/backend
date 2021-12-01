package com.sikhye.chabak.src.oauth;//package com.sikhye.utils.oauth;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.sikhye.base.BaseException;
//import com.sikhye.utils.JwtService;
//import com.sikhye.utils.oauth.social.SocialLoginType;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//import javax.validation.constraints.NotNull;
//import java.io.IOException;
//import java.util.List;
//
//import static com.sikhye.base.BaseResponseStatus.CHECK_USER;
//import static com.sikhye.base.BaseResponseStatus.EMPTY_OR_INVALID_TOKEN;
//
//@Service
//@Slf4j
//public class OAuthService {
//	private final List<SocialOAuth> socialOauthList;
//	private final HttpServletResponse response;
//	private final ProfileProvider profileProvider;
//	private final JwtService jwtService;
//
//	public OAuthService(List<SocialOAuth> socialOauthList, HttpServletResponse response, ProfileProvider profileProvider, JwtService jwtService) {
//		this.socialOauthList = socialOauthList;
//		this.response = response;
//		this.profileProvider = profileProvider;
//		this.jwtService = jwtService;
//	}
//
//	public void request(SocialLoginType socialLoginType) {
//		SocialOAuth socialOauth = this.findSocialOAuthByType(socialLoginType);
//		String redirectURL = socialOauth.getOauthRedirectURL();
//		try {
//			response.sendRedirect(redirectURL);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public PostLoginRes requestAccessToken(SocialLoginType socialLoginType, String code) throws BaseException {
//
//		// google에 대한 정보가 담김
//		SocialOAuth socialOAuth = this.findSocialOAuthByType(socialLoginType);
//
//		@Valid @NotNull
//		String email = "";
//		// token에서 email 추출 -> DB에 해당하는 유저 있는지 확인
//		try {
//			email = socialOAuth.requestAccessToken(code);
//		} catch (JsonProcessingException ex) {
//			throw new BaseException(EMPTY_OR_INVALID_TOKEN);
//		}
//
//		// 1) DB (email) 해당하는 유저가 없으면 에러처리, 있으면 정상값 return
//		UserDTO user = profileProvider.getUser(email);
//
//		if (user == null) {
//			log.info(email);
//			throw new BaseException(CHECK_USER);
//		} else {
//			log.info(email);
//			String jwt = jwtService.createJwt(user.getUserIdx().intValue());
//
//			return new PostLoginRes(jwt, user.getUserIdx().intValue());
//		}
//	}
//
//
//	private SocialOAuth findSocialOAuthByType(SocialLoginType socialLoginType) {
//		return socialOauthList.stream()
//			.filter(x -> x.type() == socialLoginType)
//			.findFirst()
//			.orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
//	}
//}
