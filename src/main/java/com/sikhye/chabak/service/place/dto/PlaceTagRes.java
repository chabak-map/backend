package com.sikhye.chabak.service.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
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
