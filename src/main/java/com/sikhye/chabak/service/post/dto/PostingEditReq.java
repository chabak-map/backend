package com.sikhye.chabak.service.post.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostingEditReq {

	@NotBlank
	private String content;

	private List<String> images;

	private List<String> tags;

	public PostingEditReq() {
	}

	@Builder
	public PostingEditReq(String content, List<String> images, List<String> tags) {
		this.content = content;
		this.images = images;
		this.tags = tags;
	}
}
