package com.sikhye.chabak.utils;

import com.sikhye.chabak.base.exception.BaseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.sikhye.chabak.base.BaseResponseStatus.EMPTY_JWT;
import static com.sikhye.chabak.base.BaseResponseStatus.INVALID_JWT;

@Service
public class JwtService {

	@Value("${secret.JWT_SECRET_KEY}")
	private String JWT_SECRET_KEY;

	@Value("${secret.USER_INFO_PASSWORD_KEY}")
	private String USER_INFO_PASSWORD_KEY;


	/*
	JWT 생성
	@param userIdx
	@return String
	 */
	public String createJwt(Long userIdx) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("userIdx", userIdx)
			.setIssuedAt(now)
			.setExpiration(new Date(System.currentTimeMillis() + 1 * (1000 * 60 * 60 * 24 * 365)))
			.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
			.compact();
	}

	/*
	Header에서 X-ACCESS-TOKEN 으로 JWT 추출
	@return String
	 */
	public String getJwt() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getHeader("X-ACCESS-TOKEN");
	}

	/*
	JWT에서 userIdx 추출
	@return int
	@throws BaseException
	 */
	public Long getUserIdx() throws BaseException {
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
//        return claims.getBody().get("userIdx",Integer.class);
		// >> pt2 : Object To Long
		return Long.valueOf(String.valueOf(claims.getBody().get("userIdx")));
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
//		return claims.getBody().get("userIdx",Integer.class);
		return Long.valueOf(String.valueOf(claims.getBody().get("userIdx")));
	}

}
