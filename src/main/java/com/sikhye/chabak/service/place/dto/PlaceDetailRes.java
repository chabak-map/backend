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
	private List<String> placeImageUrls;
	private List<String> tagNames;
	private List<PlaceCommentRes> commentResList;

	public PlaceDetailRes() {
	}

	@Builder
	public PlaceDetailRes(Long id, String name, String address, long reviewCount, long imageCount, String phoneNumber,
		List<String> placeImageUrls, List<String> tagNames, List<PlaceCommentRes> commentResList) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.reviewCount = reviewCount;
		this.imageCount = imageCount;
		this.phoneNumber = phoneNumber;
		this.placeImageUrls = placeImageUrls;
		this.tagNames = tagNames;
		this.commentResList = commentResList;
	}
}
