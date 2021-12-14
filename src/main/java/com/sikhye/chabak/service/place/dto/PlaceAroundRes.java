package com.sikhye.chabak.service.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceAroundRes {

	private Long placeId;
	private Double distance;
	private Double latitude;
	private Double longitude;

	@Builder
	public PlaceAroundRes(Long placeId, Double distance, Double latitude, Double longitude) {
		this.placeId = placeId;
		this.distance = distance;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public PlaceAroundRes() {
	}
}
