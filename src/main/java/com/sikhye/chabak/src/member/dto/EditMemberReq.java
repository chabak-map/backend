package com.sikhye.chabak.src.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditMemberReq {

	@NotBlank
	private String nickname;

	private MultipartFile image;

	@Builder
	public EditMemberReq(String nickname, MultipartFile image) {
		this.nickname = nickname;
		this.image = image;
	}
}
