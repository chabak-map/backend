package com.sikhye.chabak.service.oauth.social;

import com.sikhye.chabak.service.oauth.constant.OAuthType;
import com.sikhye.chabak.service.oauth.dto.UserInfo;

public interface SocialOAuth {
	/**
	 * 각 Social Login 페이지로 Redirect 처리할 URL Build
	 * 사용자로부터 로그인 요청을 받아 Social Login Server 인증용 code 요청
	 */
	String getOAuthRedirectUrl();

	/**
	 * API Server로부터 받은 code를 활용하여 사용자 인증 정보 요청
	 * @param authorizedCode API Server 에서 받아온 code
	 * @return API 서버로 부터 응답받은 Json 형태의 결과를 UserInfo 반환
	 */
	UserInfo getUserInfo(String authorizedCode) throws Exception;

	String getInitPassword();

	default OAuthType type() {
		if (this instanceof GoogleOAuthImpl) {
			return OAuthType.GOOGLE;
		} else if (this instanceof KakaoOAuthImpl) {
			return OAuthType.KAKAO;
		} else {
			return null;
		}
	}
}
