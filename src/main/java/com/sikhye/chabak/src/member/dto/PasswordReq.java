package com.sikhye.chabak.src.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordReq {

	private Long memberId;

	@NotBlank
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Builder
	public PasswordReq(Long memberId, String password) {
		this.memberId = memberId;
		this.password = password;
	}
}
