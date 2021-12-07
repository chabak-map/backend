package com.sikhye.chabak.service.jwt;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static com.sikhye.chabak.service.jwt.constant.JwtValue.*;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sikhye.chabak.global.config.ConfigProperties;
import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.member.constant.BaseRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenService {

	private final ConfigProperties properties;

	public JwtTokenService(ConfigProperties properties) {
		this.properties = properties;
	}

	// 토큰 유효기간 4일
	private final long tokenValidTime = 1000L * 60 * 60 * 24 * 4;

	// JWT 토큰 생성
	public String createJwt(Long memberId, BaseRole role) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("memberId", memberId)
			.claim("role", role.name())
			.setIssuedAt(now) // 토큰 발행 시간 정보
			.setExpiration(new Date(System.currentTimeMillis() + tokenValidTime)) // set Expire Time
			.signWith(SignatureAlgorithm.HS256, properties.getJwtSecret())
			.compact();
	}

	/*
		Header에서 X-ACCESS-TOKEN 으로 JWT 추출
		@return String
 	*/
	public String getJwt() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getHeader(X_ACCESS_TOKEN.toString());
	}

	/*
		JWT에서 유저권한 추출
		@return String
		@throws BaseException
 	*/
	public BaseRole getMemberRole() throws BaseException {
		//1. JWT 추출
		String accessToken = getJwt();
		if (accessToken == null || accessToken.length() == 0) {
			throw new BaseException(EMPTY_JWT);
		}

		// 2. JWT parsing
		Jws<Claims> claims;
		try {
			claims = Jwts.parser()
				.setSigningKey(properties.getJwtSecret())
				.parseClaimsJws(accessToken);
		} catch (Exception ignored) {
			throw new BaseException(INVALID_JWT);
		}

		// 3. userIdx 추출
		// ptpt: Object To Long
		return BaseRole.valueOf(String.valueOf(claims.getBody().get("role")));
	}

	/*
		JWT에서 userIdx 추출
		@return int
		@throws BaseException
 	*/
	public Long getMemberId() throws BaseException {
		//1. JWT 추출
		String accessToken = getJwt();
		if (accessToken == null || accessToken.length() == 0) {
			throw new BaseException(EMPTY_JWT);
		}

		// 2. JWT parsing
		Jws<Claims> claims;
		try {
			claims = Jwts.parser()
				.setSigningKey(properties.getJwtSecret())
				.parseClaimsJws(accessToken);
		} catch (Exception ignored) {
			throw new BaseException(INVALID_JWT);
		}

		// 3. userIdx 추출
		// ptpt: Object To Long
		return Long.valueOf(String.valueOf(claims.getBody().get("memberId")));
	}

	// 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser()
				.setSigningKey(properties.getUserInfoPasswordKey())
				.parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}
