package com.sikhye.chabak.service.jwt;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.member.constant.BaseRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenService {

	@Value("${secret.JWT_SECRET_KEY}")
	private String JWT_SECRET_KEY;

	/*
	JWT 생성
	@param userIdx
	@return String
	 */
	public String createJwt(Long memberId, BaseRole role) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("memberId", memberId)
			.claim("role", role.toString())
			.setIssuedAt(now)
			.setExpiration(new Date(System.currentTimeMillis() + 1 * (1000 * 60 * 60 * 24 * 365)))    // TODO: 토큰유효 3일
			.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
			.compact();
	}

	/*
	Header에서 X-ACCESS-TOKEN 으로 JWT 추출
	@return String
	 */
	public String getJwt() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getHeader("X-ACCESS-TOKEN");
	}

	/*
	JWT에서 유저권한 추출
	@return String
	@throws BaseException
	 */
	public String getMemberRole() throws BaseException {
		//1. JWT 추출
		String accessToken = getJwt();
		if (accessToken == null || accessToken.length() == 0) {
			throw new BaseException(EMPTY_JWT);
		}

		// 2. JWT parsing
		Jws<Claims> claims;
		try {
			claims = Jwts.parser()
				.setSigningKey(JWT_SECRET_KEY)
				.parseClaimsJws(accessToken);
		} catch (Exception ignored) {
			throw new BaseException(INVALID_JWT);
		}

		// 3. userIdx 추출
		// ptpt: Object To Long
		return String.valueOf(claims.getBody().get("role"));
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
				.setSigningKey(JWT_SECRET_KEY)
				.parseClaimsJws(accessToken);
		} catch (Exception ignored) {
			throw new BaseException(INVALID_JWT);
		}

		// 3. userIdx 추출
		// ptpt: Object To Long
		return Long.valueOf(String.valueOf(claims.getBody().get("memberId")));
	}

	public Long getUserEmail(String token) throws BaseException {
		//1. JWT 추출
		if (token == null || token.length() == 0) {
			throw new BaseException(EMPTY_JWT);
		}

		// 2. JWT parsing
		Jws<Claims> claims;
		try {
			claims = Jwts.parser()
				.setSigningKey(JWT_SECRET_KEY)
				.parseClaimsJws(token);
		} catch (Exception ignored) {
			throw new BaseException(INVALID_JWT);
		}

		// 3. userIdx 추출
		return Long.valueOf(String.valueOf(claims.getBody().get("memberId")));
	}

}
