package com.sikhye.chabak.service.post.dto;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostingCommentReq {

	@NotBlank
	private String content;

	public PostingCommentReq() {
	}

	@Builder
	public PostingCommentReq(String content) {
		this.content = content;
	}

}
