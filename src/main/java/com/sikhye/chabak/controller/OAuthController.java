package com.sikhye.chabak.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.service.member.dto.LoginRes;
import com.sikhye.chabak.service.oauth.OAuthService;
import com.sikhye.chabak.service.oauth.constant.OAuthType;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping(value = "/auth")
@Slf4j
public class OAuthController {

	private final OAuthService oAuthService;

	public OAuthController(OAuthService oAuthService) {
		this.oAuthService = oAuthService;
	}

	/**
	 * [53] 사용자 SNS 로그인 요청
	 */
	@GetMapping(value = "/{socialName}")
	public void socialLoginType(
		@PathVariable(name = "socialName") OAuthType socialLoginType) {
		log.info(">> 사용자로부터 로그인 요청 받음 :: {} Social Login", socialLoginType);
		oAuthService.requestConnect(socialLoginType);
	}

	/**
	 * OAUTH API Server 요청에 의한 callback 을 처리
	 */
	@GetMapping(value = "/{socialName}/callback")
	public BaseResponse<LoginRes> callback(@PathVariable(name = "socialName") OAuthType socialLoginType,
		@RequestParam(name = "code") String code) throws Exception {
		try {
			log.info(">> 소셜 로그인 API 서버 응답 code :: {}", code);
			return new BaseResponse<>(oAuthService.socialLogin(socialLoginType, code));
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
	}

}
