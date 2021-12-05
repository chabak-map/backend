package com.sikhye.chabak.service.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingRes {

	private String title;
	private String nickname;
	private String imageUrl;
	private Long commentCount;
	private LocalDate createdAt;

	public PostingRes() {
	}

	@Builder
	public PostingRes(String title, String nickname, String imageUrl, Long commentCount, LocalDate createdAt) {
		this.title = title;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
		this.commentCount = commentCount;
		this.createdAt = createdAt;
	}
}
