package com.sikhye.chabak.service.member.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Getter
public class MemberDto {

	private Long id;

	@Length(min = 3, max = 20)
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$")
	private String nickname;

	private String imageUrl;

	public MemberDto() {
	}

	@Builder
	public MemberDto(Long id, String nickname, String imageUrl) {
		this.id = id;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
	}
}

