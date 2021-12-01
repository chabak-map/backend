package com.sikhye.chabak.src.member.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
public class EditMemberReq {

	@NotBlank
	private String nickname;

	private MultipartFile image;

	public EditMemberReq() {
	}

	@Builder
	public EditMemberReq(String nickname, MultipartFile image) {
		this.nickname = nickname;
		this.image = image;
	}
}
