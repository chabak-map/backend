package com.sikhye.chabak.service.oauth.social;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.sikhye.chabak.service.oauth.config.OAuthProperties;
import com.sikhye.chabak.service.oauth.dto.GoogleOAuthResponse;
import com.sikhye.chabak.service.oauth.dto.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GoogleOAuthImpl implements SocialOAuth {

	private final OAuthProperties oAuthProperties;

	public GoogleOAuthImpl(OAuthProperties oAuthProperties) {
		this.oAuthProperties = oAuthProperties;
	}

	@Override
	public UserInfo getUserInfo(String authorizedCode) throws Exception {
		log.info(">> 받은 인증코드 : {}", authorizedCode);

		String code = URLDecoder.decode(authorizedCode, StandardCharsets.UTF_8.name());
		log.info(">> decode된 인증코드 : {}", code);

		String accessToken = requestAccessToken(code);
		UserInfo userInfo = getUserInfoByToken(accessToken);

		return userInfo;
	}

	@Override
	public String getInitPassword() {
		return oAuthProperties.getGoogle().getClientSecret();
	}

	@Override
	public String getOAuthRedirectUrl() {
		Map<String, Object> params = new HashMap<>();
		params.put("scope", "openid profile email phone");
		params.put("response_type", "code");
		params.put("client_id", oAuthProperties.getGoogle().getClientId());
		params.put("redirect_uri", oAuthProperties.getGoogle().getCallbackUrl());

		String parameterString = params.entrySet().stream()
			.map(x -> x.getKey() + "=" + x.getValue())
			.collect(Collectors.joining("&"));

		return "https://accounts.google.com/o/oauth2/v2/auth?" + parameterString;
	}

	// ============================================
	// INTERNAL USE
	// ============================================
	private String requestAccessToken(String authorizedCode) throws Exception {

		RestTemplate restTemplate = new RestTemplate();

		Map<String, Object> params = new HashMap<>();
		params.put("code", authorizedCode);
		params.put("client_id", oAuthProperties.getGoogle().getClientId());
		params.put("client_secret", oAuthProperties.getGoogle().getClientSecret());
		params.put("redirect_uri", oAuthProperties.getGoogle().getCallbackUrl());
		params.put("grant_type", "authorization_code");

		//AccessToken 발급 요청
		ResponseEntity<String> resultEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
			params,
			String.class);

		log.info("resultEntity.getBody() => {}", resultEntity.getBody());

		//JSON 파싱 기본값 세팅
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		//Token Request
		GoogleOAuthResponse googleOAuthResponse = mapper.readValue(resultEntity.getBody(), new TypeReference<>() {
		});

		return googleOAuthResponse.getIdToken();

	}

	private UserInfo getUserInfoByToken(String token) throws Exception {

		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();

		String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
			.queryParam("id_token", token).encode().toUriString();

		String resultJson = restTemplate.getForObject(requestUrl, String.class);

		// userinfo만 추출
		Map<String, String> userInfo = mapper.readValue(resultJson, new TypeReference<>() {
		});

		String id = userInfo.get("sub");
		String email = userInfo.get("email");
		String name = userInfo.get("name");

		return new UserInfo(id, email, name);

	}

}
