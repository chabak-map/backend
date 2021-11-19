package com.sikhye.chabak.src.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

	private Long id;

	@Length(min=3, max = 20)
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$")
	private String nickname;

	private String imageUrl;

}
