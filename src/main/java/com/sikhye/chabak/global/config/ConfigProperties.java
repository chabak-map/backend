package com.sikhye.chabak.global.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
@ConfigurationProperties(prefix = "secret")    // >> ptpt : configurationProperties
public class ConfigProperties {

	@NotBlank
	private String jwtSecret;

	@NotBlank
	private String userInfoPasswordKey;

	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

	public void setUserInfoPasswordKey(String userInfoPasswordKey) {
		this.userInfoPasswordKey = userInfoPasswordKey;
	}
}
