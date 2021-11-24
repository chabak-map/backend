package com.sikhye.chabak.src.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceReviewRes {

	private String name;
	private String content;
	private LocalDate writingDate;

	@Builder
	public PlaceReviewRes(String name, String content, LocalDate writingDate) {
		this.name = name;
		this.content = content;
		this.writingDate = writingDate;
	}
}
