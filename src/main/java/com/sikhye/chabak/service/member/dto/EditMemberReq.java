package com.sikhye.chabak.service.member.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EditMemberReq {

	@NotBlank
	private String nickname;

	private MultipartFile image;

	public EditMemberReq() {
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	@Builder
	public EditMemberReq(String nickname, MultipartFile image) {
		this.nickname = nickname;
		this.image = image;
	}

	@Builder
	public EditMemberReq(String nickname) {
		this.nickname = nickname;
	}
}
