package com.sikhye.chabak.service.oauth.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "oauth2.client")
public class OAuthProperties {

	private final Google google;
	private final Kakao kakao;

	public OAuthProperties(Google google, Kakao kakao) {
		this.google = google;
		this.kakao = kakao;
	}

	@Getter
	@Validated
	public static class Kakao {
		@NotBlank
		private final String clientId;

		@NotBlank
		private final String clientSecret;

		@NotBlank
		private final String callbackUrl;

		public Kakao(String clientId, String clientSecret, String callbackUrl) {
			this.clientId = clientId;
			this.clientSecret = clientSecret;
			this.callbackUrl = callbackUrl;
		}
	}

	@Getter
	@Validated
	public static class Google {
		@NotBlank
		private final String clientId;

		@NotBlank
		private final String clientSecret;

		@NotBlank
		private final String callbackUrl;

		public Google(String clientId, String clientSecret, String callbackUrl) {
			this.clientId = clientId;
			this.clientSecret = clientSecret;
			this.callbackUrl = callbackUrl;
		}
	}
}
