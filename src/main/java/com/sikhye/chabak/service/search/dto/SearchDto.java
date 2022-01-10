package com.sikhye.chabak.service.search.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchDto {

	List<SearchPlaceRes> places;
	List<SearchPostRes> posts;

	public SearchDto() {
	}

	@Builder
	public SearchDto(List<SearchPlaceRes> places, List<SearchPostRes> posts) {
		this.places = places;
		this.posts = posts;
	}
}

