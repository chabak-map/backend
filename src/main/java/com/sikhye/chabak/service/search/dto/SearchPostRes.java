package com.sikhye.chabak.service.search.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchPostRes {

	private Long id;
	private String title;
	private String content;    // 30자 잘라서 보내기
	private String imageUrl;

	public SearchPostRes() {
	}

	@Builder
	public SearchPostRes(Long id, String title, String content, String imageUrl) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
	}

}
