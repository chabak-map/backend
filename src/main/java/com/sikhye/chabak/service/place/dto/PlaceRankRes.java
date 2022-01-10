package com.sikhye.chabak.service.place.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceRankRes {

	private Integer viewCount;

	private Long placeId;
	private String name;

	private String address;
	private String placeImageUrl;

	public PlaceRankRes() {
	}

	@Builder
	public PlaceRankRes(Integer viewCount, Long placeId, String name, String address, String placeImageUrl) {
		this.viewCount = viewCount;
		this.placeId = placeId;
		this.name = name;
		this.address = address;
		this.placeImageUrl = placeImageUrl;
	}
}
