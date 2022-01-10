package com.sikhye.chabak.service.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PasswordReq {

	private Long memberId;

	@NotBlank
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	public PasswordReq() {
	}

	@Builder
	public PasswordReq(Long memberId, String password) {
		this.memberId = memberId;
		this.password = password;
	}
}
