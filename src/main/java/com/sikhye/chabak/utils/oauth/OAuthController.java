package com.sikhye.chabak.utils.oauth;//package com.sikhye.utils.oauth;
//
//
//import com.sikhye.base.BaseException;
//import com.sikhye.base.BaseResponse;
//import com.sikhye.utils.oauth.social.SocialLoginType;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@CrossOrigin
//@RequestMapping(value = "/auth")
//@Slf4j
//public class OAuthController {
//
//	private final OAuthService oAuthService;
//
//	public OAuthController(OAuthService oAuthService) {
//		this.oAuthService = oAuthService;
//	}
//
//	/**
//	 * [53] 사용자 SNS 로그인 요청
//	 */
//	@PostMapping(value = "/{socialName}")
//	public void socialLoginType(
//		@PathVariable(name = "socialName") SocialLoginType socialLoginType) {
//		log.info(">> 사용자로부터 로그인 요청 받음 :: {} Social Login", socialLoginType);
//		oAuthService.request(socialLoginType);
//	}
//
//	/**
//	 * OAUTH API Server 요청에 의한 callback 을 처리
//	 */
//	@GetMapping(value = "/{socialName}/callback")
//	public BaseResponse<PostLoginRes> callback(@PathVariable(name = "socialName") SocialLoginType socialLoginType,
//											   @RequestParam(name = "code") String code) {
//		try {
//			log.info(">> 소셜 로그인 API 서버 응답 code :: {}", code);
//			return new BaseResponse<>(oAuthService.requestAccessToken(socialLoginType, code));
//		} catch (BaseException exception) {
//			return new BaseResponse<>(exception.getStatus());
//		}
//	}
//
//}
