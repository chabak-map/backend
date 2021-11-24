package com.sikhye.chabak.src.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceSearchRes {

	private Long placeId;
	private Double distance;
	private Double latitude;
	private Double longitude;

	@Builder
	public PlaceSearchRes(Long placeId, Double distance, Double latitude, Double longitude) {
		this.placeId = placeId;
		this.distance = distance;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
