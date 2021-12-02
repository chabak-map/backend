package com.sikhye.chabak.interceptor;

import com.sikhye.chabak.base.exception.BaseException;
import com.sikhye.chabak.utils.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.sikhye.chabak.base.BaseResponseStatus.EMPTY_JWT;

@Slf4j
@Component
public class JwtMemberInterceptor implements HandlerInterceptor {

	private final JwtTokenService jwtTokenService;

	public JwtMemberInterceptor(JwtTokenService jwtTokenService) {
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws Exception {

		String requestURI = request.getRequestURI();
		String accessToken = request.getHeader("X-ACCESS-TOKEN");

		log.info("인증 체크 인터셉터 실행 {}", requestURI);

		if (accessToken == null || accessToken.isBlank()) {
			log.info("미인증 JWT 요청");
			throw new BaseException(EMPTY_JWT);
		}

		return true;
	}

}
