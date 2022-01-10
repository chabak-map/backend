package com.sikhye.chabak.service.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
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

