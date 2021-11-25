package com.sikhye.chabak.src.tag.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceTagRes {

	private Long placeTagId;
	private String placeTagName;

	@Builder
	public PlaceTagRes(Long placeTagId, String placeTagName) {
		this.placeTagId = placeTagId;
		this.placeTagName = placeTagName;
	}
}
