package com.sikhye.chabak.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.service.search.SearchService;
import com.sikhye.chabak.service.search.dto.SearchDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

	private final SearchService searchService;

	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}

	@GetMapping
	public BaseResponse<SearchDto> search(@RequestParam String q) {

		SearchDto searchDto;
		// if (q.startsWith("#")) {
		// >> ptpt: 한 글자만 비교하는거는 charAt이 성능상 좋다.
		if (q.charAt(0) == '#') {
			searchDto = searchService.searchByTag(q.substring(1));
		} else {
			searchDto = searchService.searchBy(q);
		}

		return new BaseResponse<>(searchDto);
	}

}
