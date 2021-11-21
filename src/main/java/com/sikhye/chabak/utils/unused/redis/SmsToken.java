//package com.sikhye.chabak.utils.sms.entity;
//
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.redis.core.RedisHash;
//
//@Getter
//@RedisHash(value = "smstoken", timeToLive = 300)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class SmsToken {
//
//	@Id
//	private String phoneNumber;
//
//	private String verifyCode;
//
//
//	@Builder
//	public SmsToken(String verifyCode, String phoneNumber) {
//		this.verifyCode = verifyCode;
//		this.phoneNumber = phoneNumber;
//	}
//}
