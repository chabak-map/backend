package com.sikhye.chabak.service.post.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingRes {

	private Long id;
	private String title;
	private String content;
	private String nickname;
	private List<String> imageUrls;
	private Long commentCount;
	private LocalDate createdAt;

	public PostingRes() {
	}

	@Builder
	public PostingRes(Long id, String title, String content, String nickname, List<String> imageUrls,
		Long commentCount, LocalDate createdAt) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.nickname = nickname;
		this.imageUrls = imageUrls;
		this.commentCount = commentCount;
		this.createdAt = createdAt;
	}
}
