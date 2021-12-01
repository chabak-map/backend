package com.sikhye.chabak.src.tag.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceTagRes {

	private Long placeTagId;
	private String placeTagName;

	public PlaceTagRes() {
	}

	@Builder
	public PlaceTagRes(Long placeTagId, String placeTagName) {
		this.placeTagId = placeTagId;
		this.placeTagName = placeTagName;
	}
}
