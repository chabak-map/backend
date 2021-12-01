package com.sikhye.chabak.src.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookmarkRes {

	// 이름
	private String name;

	// 주소
	private String address;

	// 대표 이미지
	private String imageUrl;

	// 장소 태그들
	private List<String> placeTagNames;

	public BookmarkRes() {

	}

	@Builder
	public BookmarkRes(String name, String address, String imageUrl, List<String> placeTagNames) {
		this.name = name;
		this.address = address;
		this.imageUrl = imageUrl;
		this.placeTagNames = placeTagNames;
	}
}
