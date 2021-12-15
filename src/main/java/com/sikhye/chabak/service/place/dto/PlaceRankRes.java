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

	// >> ptpt: stream() 결과 반환 시 간단하게 해당 메소드로 사용 가능
	// public PlaceRankRes convertToPlaceRankRes() {
	// }
}
