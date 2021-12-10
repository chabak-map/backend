package com.sikhye.chabak.service.place.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceTagReq {

	List<String> placeTags;

	@Builder
	public PlaceTagReq(List<String> placeTags) {
		this.placeTags = placeTags;
	}

	public PlaceTagReq() {
	}
}
