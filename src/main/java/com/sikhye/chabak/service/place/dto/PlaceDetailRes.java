package com.sikhye.chabak.service.place.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDetailRes {

	private Long id;
	private String name;
	private String address;
	private long reviewCount;
	private long imageCount;
	private String phoneNumber;
	private Double distance;
	private String url;
	private List<String> placeImageUrls;
	private List<String> tagNames;
	private List<PlaceCommentRes> commentResList;

	public PlaceDetailRes() {
	}

	@Builder
	public PlaceDetailRes(Long id, String name, String address, long reviewCount, long imageCount,
		String phoneNumber, Double distance, String url, List<String> placeImageUrls,
		List<String> tagNames, List<PlaceCommentRes> commentResList) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.reviewCount = reviewCount;
		this.imageCount = imageCount;
		this.phoneNumber = phoneNumber;
		this.distance = distance;
		this.url = url;
		this.placeImageUrls = placeImageUrls;
		this.tagNames = tagNames;
		this.commentResList = commentResList;
	}
}
