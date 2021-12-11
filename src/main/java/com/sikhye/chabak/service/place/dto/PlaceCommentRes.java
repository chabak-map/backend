package com.sikhye.chabak.service.place.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceCommentRes {

	private String name;
	private String content;
	private LocalDate writingDate;

	public PlaceCommentRes() {
	}

	@Builder
	public PlaceCommentRes(String name, String content, LocalDate writingDate) {
		this.name = name;
		this.content = content;
		this.writingDate = writingDate;
	}
}
