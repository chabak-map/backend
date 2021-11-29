package com.sikhye.chabak.src.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingRes {

	private String title;
	private String nickname;
	private String imageUrl;
	private Long commentCount;
	private LocalDate createdAt;

	@Builder
	public PostingRes(String title, String nickname, String imageUrl, Long commentCount, LocalDate createdAt) {
		this.title = title;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
		this.commentCount = commentCount;
		this.createdAt = createdAt;
	}
}
