package com.sikhye.chabak.service.tag.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
