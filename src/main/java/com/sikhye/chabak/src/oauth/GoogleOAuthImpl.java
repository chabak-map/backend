package com.sikhye.chabak.src.oauth;//package com.sikhye.univcity.utils.oauth;
//
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.PropertyNamingStrategy;
//import com.sikhye.univcity.utils.oauth.social.GoogleOAuthResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class GoogleOAuthImpl implements SocialOAuth {
//	@Value("${oauth2.client.google.base-url}")
//	private String GOOGLE_SNS_BASE_URL;
//	@Value("${oauth2.client.google.client-id}")
//	private String GOOGLE_SNS_CLIENT_ID;
//	@Value("${oauth2.client.google.callback-url}")
//	private String GOOGLE_SNS_CALLBACK_URL;
//	@Value("${oauth2.client.google.client-secret}")
//	private String GOOGLE_SNS_CLIENT_SECRET;
//	@Value("${oauth2.client.google.token-url}")
//	private String GOOGLE_SNS_TOKEN_BASE_URL;
//
//	@Override
//	public String getOauthRedirectURL() {
//		Map<String, Object> params = new HashMap<>();
//		params.put("scope", "profile email");
//		params.put("response_type", "code");
//		params.put("client_id", GOOGLE_SNS_CLIENT_ID);
//		params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
//
//		String parameterString = params.entrySet().stream()
//			.map(x -> x.getKey() + "=" + x.getValue())
//			.collect(Collectors.joining("&"));
//
//		return GOOGLE_SNS_BASE_URL + "?" + parameterString;
//	}
//
//	@Override
//	public String requestAccessToken(String code) throws JsonProcessingException {
//		RestTemplate restTemplate = new RestTemplate();
//
//		Map<String, Object> params = new HashMap<>();
//		params.put("code", code);
//		params.put("client_id", GOOGLE_SNS_CLIENT_ID);
//		params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
//		params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
//		params.put("grant_type", "authorization_code");
//
//		//JSON 파싱 기본값 세팅
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
//		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//		//AccessToken 발급 요청
//		ResponseEntity<String> resultEntity = restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);
//
//		//Token Request
//		GoogleOAuthResponse result = mapper.readValue(resultEntity.getBody(), new TypeReference<GoogleOAuthResponse>() {});
//
//		//ID Token만 추출 (사용자의 정보는 jwt로 인코딩 되어있다)
//		String jwtToken = result.getIdToken();
//		String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
//			.queryParam("id_token", jwtToken).encode().toUriString();
//
//		// 추출된 token ( 여기서 email만 사용 )
//		String resultJson = restTemplate.getForObject(requestUrl, String.class);
//
//		// userinfo만 추출
//		Map<String,String> userInfo = mapper.readValue(resultJson, new TypeReference<Map<String, String>>(){});
//
//		return userInfo.isEmpty() ? null : userInfo.get("email");
//
//	}
//
//
////	@Override
////	public String requestAccessToken(String code) throws JsonProcessingException {
////		RestTemplate restTemplate = new RestTemplate();
////
////		Map<String, Object> params = new HashMap<>();
////		params.put("code", code);
////		params.put("client_id", GOOGLE_SNS_CLIENT_ID);
////		params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
////		params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
////		params.put("grant_type", "authorization_code");
////
////		//JSON 파싱을 위한 기본값 세팅
////		//요청시 파라미터는 스네이크 케이스로 세팅되므로 Object mapper에 미리 설정해준다.
////		ObjectMapper mapper = new ObjectMapper();
////		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
////		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
////
////		// AccessToken 발급요청
////		ResponseEntity<String> responseEntity =
////			restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);
////
////		//Token Request
////		GoogleOAuthResponse result = mapper.readValue(responseEntity.getBody(), new TypeReference<GoogleOAuthResponse>() {
////		});
////
////		if (responseEntity.getStatusCode() == HttpStatus.OK) {
////			return responseEntity.getBody();
////		}
////
////
//////		if (responseEntity.getStatusCode() == HttpStatus.OK) {
//////			return responseEntity.getBody();
//////		}
////		return "구글 로그인 요청 처리 실패";
////	}
//
//}
