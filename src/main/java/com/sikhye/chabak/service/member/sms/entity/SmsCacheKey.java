package com.sikhye.chabak.service.member.sms.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsCacheKey {
	public static final int DEFAULT_EXPIRE_SEC = 300;
	public static final String SMS = "sms";
	public static final int SMS_EXPIRE_SEC = 300;
}
