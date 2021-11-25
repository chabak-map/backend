package com.sikhye.chabak.src.tag.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostingTagReq {

	Long postingId;
	List<String> postingTags;

	@Builder
	public PostingTagReq(Long postingId, List<String> postingTags) {
		this.postingId = postingId;
		this.postingTags = postingTags;
	}
}
