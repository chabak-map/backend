package com.sikhye.chabak.src.tag.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceTagReq {

	List<String> placeTags;

	@Builder
	public PlaceTagReq(List<String> placeTags) {
		this.placeTags = placeTags;
	}
}
