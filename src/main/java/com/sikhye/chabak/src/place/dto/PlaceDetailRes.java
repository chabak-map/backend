package com.sikhye.chabak.src.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDetailRes {

	private String name;
	private String address;
	private long reviewCount;
	private long imageCount;
	private String phoneNumber;
	private List<String> placeImageUrls;
	private List<String> tagNames;
	private List<PlaceReviewRes> placeReviewResList;

	@Builder

	public PlaceDetailRes(String name, String address, long reviewCount, long imageCount, String phoneNumber, List<String> placeImageUrls, List<String> tagNames, List<PlaceReviewRes> placeReviewResList) {
		this.name = name;
		this.address = address;
		this.reviewCount = reviewCount;
		this.imageCount = imageCount;
		this.phoneNumber = phoneNumber;
		this.placeImageUrls = placeImageUrls;
		this.tagNames = tagNames;
		this.placeReviewResList = placeReviewResList;
	}
}
