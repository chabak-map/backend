package com.sikhye.chabak.service.post.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostingTagReq {

	Long postingId;
	List<String> postingTags;

	public PostingTagReq() {
	}

	@Builder
	public PostingTagReq(Long postingId, List<String> postingTags) {
		this.postingId = postingId;
		this.postingTags = postingTags;
	}
}
