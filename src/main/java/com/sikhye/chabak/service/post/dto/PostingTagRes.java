package com.sikhye.chabak.service.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
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


