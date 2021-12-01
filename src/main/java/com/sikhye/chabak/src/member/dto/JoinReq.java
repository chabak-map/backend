package com.sikhye.chabak.src.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonProperty.Access;

@Getter
public class JoinReq {

	@Length(min = 3, max = 20)
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$")
	private String nickname;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	private String phoneNumber;

	public JoinReq() {
	}

	@Builder
	public JoinReq(String nickname, String email, String password, String phoneNumber) {
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
	}
}

