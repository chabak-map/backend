package com.sikhye.chabak.global.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "secret")
public class ConfigProperties {

	@NotBlank
	private final String jwtSecret;

	@NotBlank
	private final String userInfoPasswordKey;

	public ConfigProperties(String jwtSecret, String userInfoPasswordKey) {
		this.jwtSecret = jwtSecret;
		this.userInfoPasswordKey = userInfoPasswordKey;
	}
}
