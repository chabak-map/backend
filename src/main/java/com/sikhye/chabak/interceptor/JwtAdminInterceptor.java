package com.sikhye.chabak.interceptor;

import com.sikhye.chabak.base.exception.BaseException;
import com.sikhye.chabak.utils.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.sikhye.chabak.base.BaseResponseStatus.INVALID_USER_JWT;
import static com.sikhye.chabak.base.entity.BaseRole.ROLE_ADMIN;

@Slf4j
@Component
public class JwtAdminInterceptor implements HandlerInterceptor {

	private final JwtTokenService jwtTokenService;

	public JwtAdminInterceptor(JwtTokenService jwtTokenService) {
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws Exception {
		String requestURI = request.getRequestURI();

		// places에서 GET 방식이라면 Admin 기능이 아닌 Member도 사용 가능
		if (request.getMethod().equals("GET")) {
			return true;
		}

		log.info("인증 체크 인터셉터 실행 {}", requestURI);

		// 관리자 권한이 아닌 경우 api 요청 불가
		String memberRole = jwtTokenService.getMemberRole();

		if (!memberRole.equals(ROLE_ADMIN.toString())) {
			throw new BaseException(INVALID_USER_JWT);
		}

		return true;

	}
}
