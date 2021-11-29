package com.sikhye.chabak.src.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingDetailRes {

	private String title;
	private String content;
	private String nickname;
	private String profileImageUrl;
	private List<String> postImageUrls;

	@Builder
	public PostingDetailRes(String title, String content, String nickname, String profileImageUrl, List<String> postImageUrls) {
		this.title = title;
		this.content = content;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.postImageUrls = postImageUrls;
	}
}
