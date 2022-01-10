package com.sikhye.chabak.service.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceImageRes {

	private Long imageId;

	private String imageUrl;

	public PlaceImageRes() {
	}

	@Builder
	public PlaceImageRes(Long imageId, String imageUrl) {
		this.imageId = imageId;
		this.imageUrl = imageUrl;
	}
}
