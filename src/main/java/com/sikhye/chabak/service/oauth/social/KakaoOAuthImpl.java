package com.sikhye.chabak.service.oauth.social;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sikhye.chabak.service.oauth.config.OAuthProperties;
import com.sikhye.chabak.service.oauth.dto.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KakaoOAuthImpl implements SocialOAuth {

	private final OAuthProperties oAuthProperties;

	public KakaoOAuthImpl(OAuthProperties oAuthProperties) {
		this.oAuthProperties = oAuthProperties;
	}

	@Override
	public String getOAuthRedirectUrl() {
		StringBuffer url = new StringBuffer();
		url.append("https://kauth.kakao.com/oauth/authorize?");
		url.append("client_id=").append(oAuthProperties.getKakao().getClientId());
		url.append("&redirect_uri=").append(oAuthProperties.getKakao().getCallbackUrl());
		url.append("&response_type=code");

		return url.toString();
	}

	@Override
	public UserInfo getUserInfo(String authorizedCode) throws Exception {
		String accessToken = requestAccessToken(authorizedCode);
		UserInfo userInfo = getUserInfoByToken(accessToken);

		return userInfo;
	}

	@Override
	public String getInitPassword() {
		return oAuthProperties.getKakao().getClientSecret();
	}

	// ============================================
	// INTERNAL USE
	// ============================================
	private String requestAccessToken(String authorizedCode) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", oAuthProperties.getKakao().getClientId());
		params.add("redirect_uri", oAuthProperties.getKakao().getCallbackUrl());
		params.add("code", authorizedCode);

		RestTemplate rt = new RestTemplate();
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
			new HttpEntity<>(params, headers);

		ResponseEntity<String> response = rt.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoTokenRequest,
			String.class
		);

		String tokenJson = response.getBody();
		JSONObject rjson = new JSONObject(tokenJson);
		String accessToken = rjson.getString("access_token");

		return accessToken;
	}

	private UserInfo getUserInfoByToken(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		RestTemplate rt = new RestTemplate();
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

		ResponseEntity<String> response = rt.exchange(
			"https://kapi.kakao.com/v2/user/me",
			HttpMethod.POST,
			kakaoProfileRequest,
			String.class
		);

		JSONObject body = new JSONObject(response.getBody());
		String id = body.getString("id");
		String email = body.getJSONObject("kakao_account").getString("email");
		String nickname = body.getJSONObject("properties").getString("nickname");

		return new UserInfo(id, email, nickname);
	}
}
