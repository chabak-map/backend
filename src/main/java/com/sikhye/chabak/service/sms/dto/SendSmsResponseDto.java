package com.sikhye.chabak.service.sms.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendSmsResponseDto {
	private String statusCode;
	private String statusName;
	private String requestId;
	private LocalDateTime requestTime;
}
