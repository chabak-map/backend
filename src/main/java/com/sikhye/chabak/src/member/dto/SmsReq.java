package com.sikhye.chabak.src.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsReq {

	@NotBlank
	private String phoneNumber;

	private String verifyCode;

	@Builder
	public SmsReq(String phoneNumber, String verifyCode) {
		this.phoneNumber = phoneNumber;
		this.verifyCode = verifyCode;
	}

	@Builder
	public SmsReq(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
