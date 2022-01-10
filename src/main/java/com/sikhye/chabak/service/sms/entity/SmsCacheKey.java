package com.sikhye.chabak.service.sms.entity;

import lombok.Getter;

@Getter
public class SmsCacheKey {
	public static final int DEFAULT_EXPIRE_SEC = 300;
	public static final String SMS = "sms";
	public static final int SMS_EXPIRE_SEC = 300;

	public SmsCacheKey() {
	}
}
