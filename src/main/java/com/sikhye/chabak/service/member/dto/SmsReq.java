package com.sikhye.chabak.service.member.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SmsReq {

	@NotBlank
	private String phoneNumber;

	private String verifyCode;

	public SmsReq() {

	}

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
