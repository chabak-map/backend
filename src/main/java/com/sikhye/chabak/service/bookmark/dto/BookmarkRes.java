package com.sikhye.chabak.service.bookmark.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookmarkRes {

	private Long id;
	private String name;
	private String address;

	private String imageUrl;
	private List<String> placeTagNames;

	public BookmarkRes() {
	}

	@Builder
	public BookmarkRes(Long id, String name, String address, String imageUrl, List<String> placeTagNames) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.imageUrl = imageUrl;
		this.placeTagNames = placeTagNames;
	}
}
