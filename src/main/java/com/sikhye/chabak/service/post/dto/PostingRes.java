package com.sikhye.chabak.service.post.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingRes {

	private Long id;
	private String title;
	private String nickname;
	private String imageUrl;
	private Long commentCount;
	private LocalDate createdAt;

	public PostingRes() {
	}

	@Builder
	public PostingRes(Long id, String title, String nickname, String imageUrl, Long commentCount, LocalDate createdAt) {
		this.id = id;
		this.title = title;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
		this.commentCount = commentCount;
		this.createdAt = createdAt;
	}
}
