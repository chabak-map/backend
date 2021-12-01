package com.sikhye.chabak.src.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRes {

	private Long id;
	private String jwt;

	public LoginRes() {
	}

	@Builder
	public LoginRes(Long id, String jwt) {
		this.id = id;
		this.jwt = jwt;
	}
}

