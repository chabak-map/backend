package com.sikhye.chabak.src.comment.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentReq {

	@NotBlank
	private String content;

	public CommentReq() {
	}

	@Builder
	public CommentReq(String content) {
		this.content = content;
	}


}
