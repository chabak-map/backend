package com.sikhye.chabak.service.search.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchPlaceRes {

	private Long id;
	private String name;
	private String address;
	private String imageUrl;

	public SearchPlaceRes() {
	}

	@Builder
	public SearchPlaceRes(Long id, String name, String address, String imageUrl) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.imageUrl = imageUrl;
	}
}
