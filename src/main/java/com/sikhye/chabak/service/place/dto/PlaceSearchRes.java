package com.sikhye.chabak.service.place.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceSearchRes {

	private Long id;
	private String name;
	private String address;
	private Long reviewCount;
	private Double distance;
	private List<PlaceTagRes> placeTags;
	private List<PlaceImageRes> placeImages;    // TODO: 이미지는 TOP 3개만 제공
	private Boolean isBookmarked;

	public PlaceSearchRes() {

	}

	@Builder
	public PlaceSearchRes(Long id, String name, String address, Long reviewCount, Double distance,
		List<PlaceTagRes> placeTags, List<PlaceImageRes> placeImages, Boolean isBookmarked) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.reviewCount = reviewCount;
		this.distance = distance;
		this.placeTags = placeTags;
		this.placeImages = placeImages;
		this.isBookmarked = isBookmarked;
	}
}
