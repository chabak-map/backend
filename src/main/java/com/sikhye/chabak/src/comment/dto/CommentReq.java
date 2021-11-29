package com.sikhye.chabak.src.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReq {

	@NotBlank
	private String content;

	@Builder
	public CommentReq(String content) {
		this.content = content;
	}
}
