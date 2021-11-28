package com.sikhye.chabak.src.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sikhye.chabak.src.comment.dto.CommentRes;
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
	private List<CommentRes> commentResList;

	@Builder
	public PlaceDetailRes(String name, String address, long reviewCount, long imageCount, String phoneNumber, List<String> placeImageUrls, List<String> tagNames, List<CommentRes> commentResList) {
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
