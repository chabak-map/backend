package com.sikhye.chabak.src.email.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter
@RedisHash(value = "EmailToken", timeToLive = 300)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailToken {

	@Id
	private String id;
	private Long memberId;

	@Builder
	public EmailToken(String id, Long memberId) {
		this.id = id;
		this.memberId = memberId;
	}
}
