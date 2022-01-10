package com.sikhye.chabak.service.post.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostingTagReq {

	List<String> postingTags;

	public PostingTagReq() {
	}

	@Builder
	public PostingTagReq(List<String> postingTags) {
		this.postingTags = postingTags;
	}
}
