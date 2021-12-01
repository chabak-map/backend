package com.sikhye.chabak.src.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRes {

	private String name;
	private String content;
	private LocalDate writingDate;

	public CommentRes() {
	}

	@Builder
	public CommentRes(String name, String content, LocalDate writingDate) {
		this.name = name;
		this.content = content;
		this.writingDate = writingDate;
	}
}
