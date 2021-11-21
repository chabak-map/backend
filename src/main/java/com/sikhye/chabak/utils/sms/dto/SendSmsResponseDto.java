package com.sikhye.chabak.utils.sms.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendSmsResponseDto {
	private String statusCode;
	private String statusName;
	private String requestId;
	private LocalDateTime requestTime;
}

