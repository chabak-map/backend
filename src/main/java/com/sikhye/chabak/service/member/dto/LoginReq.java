package com.sikhye.chabak.service.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class LoginReq {

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	public LoginReq() {
	}

	public LoginReq(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
