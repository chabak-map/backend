package com.sikhye.chabak.service.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingDetailRes {

	private String title;
	private String content;
	private String nickname;
	private String profileImageUrl;
	private List<String> postImageUrls;

	public PostingDetailRes() {
	}

	@Builder
	public PostingDetailRes(String title, String content, String nickname, String profileImageUrl, List<String> postImageUrls) {
		this.title = title;
		this.content = content;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.postImageUrls = postImageUrls;
	}
}
