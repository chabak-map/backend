package com.sikhye.chabak.src.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRes {

	private Long id;
	private String jwt;

	public LoginRes(Long id, String jwt) {
		this.id = id;
		this.jwt = jwt;
	}
}
