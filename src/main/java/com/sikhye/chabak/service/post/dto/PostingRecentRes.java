package com.sikhye.chabak.service.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingRecentRes {

	private String title;
	private Integer reviewCount;
	private String imageUrl;

	public PostingRecentRes() {
	}

	@Builder
	public PostingRecentRes(String title, Integer reviewCount, String imageUrl) {
		this.title = title;
		this.reviewCount = reviewCount;
		this.imageUrl = imageUrl;
	}
}
