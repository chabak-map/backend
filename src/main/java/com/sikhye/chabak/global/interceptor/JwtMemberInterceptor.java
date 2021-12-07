package com.sikhye.chabak.global.interceptor;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static com.sikhye.chabak.service.jwt.constant.JwtValue.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sikhye.chabak.global.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtMemberInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {

		String requestURI = request.getRequestURI();
		String accessToken = request.getHeader(X_ACCESS_TOKEN.toString());

		log.info("인증 체크 인터셉터 실행 {}", requestURI);

		if (accessToken == null || accessToken.isBlank()) {
			log.info("미인증 JWT 요청");
			throw new BaseException(EMPTY_JWT);
		}

		return true;
	}

}
