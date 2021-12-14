package com.sikhye.chabak.service.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingTagRes {

	private Long postingTagId;
	private String postingTagName;

	public PostingTagRes() {
	}

	@Builder
	public PostingTagRes(Long postingTagId, String postingTagName) {
		this.postingTagId = postingTagId;
		this.postingTagName = postingTagName;
	}
}


