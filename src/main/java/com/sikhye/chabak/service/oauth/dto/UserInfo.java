package com.sikhye.chabak.service.oauth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

	private String id;
	private String email;
	private String nickname;

	public UserInfo() {
	}

	@Builder
	public UserInfo(String id, String email, String nickname) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
	}

}
