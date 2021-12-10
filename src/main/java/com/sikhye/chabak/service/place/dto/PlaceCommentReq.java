package com.sikhye.chabak.service.place.dto;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceCommentReq {

	@NotBlank
	private String content;

	public PlaceCommentReq() {
	}

	@Builder
	public PlaceCommentReq(String content) {
		this.content = content;
	}

}
