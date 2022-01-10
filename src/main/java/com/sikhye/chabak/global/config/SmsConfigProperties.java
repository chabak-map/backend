package com.sikhye.chabak.global.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "naver.cloud")
public class SmsConfigProperties {

	@NotBlank
	private final String accessKeyId;

	@NotBlank
	private final String secretKey;

	@NotBlank
	private final String serviceId;

	@NotBlank
	private final String from;

	public SmsConfigProperties(String accessKeyId, String secretKey, String serviceId, String from) {
		this.accessKeyId = accessKeyId;
		this.secretKey = secretKey;
		this.serviceId = serviceId;
		this.from = from;
	}
}
