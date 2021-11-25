package com.sikhye.chabak.src.tag.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostingTagRes {

	private Long postingTagId;
	private String postingTagName;

	@Builder
	public PostingTagRes(Long postingTagId, String postingTagName) {
		this.postingTagId = postingTagId;
		this.postingTagName = postingTagName;
	}
}
