package com.sikhye.chabak.service.post.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingCommentRes {

	private String name;
	private String content;
	private LocalDate writingDate;

	public PostingCommentRes() {
	}

	@Builder
	public PostingCommentRes(String name, String content, LocalDate writingDate) {
		this.name = name;
		this.content = content;
		this.writingDate = writingDate;
	}
}
