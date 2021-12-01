package com.sikhye.chabak.src.tag.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
