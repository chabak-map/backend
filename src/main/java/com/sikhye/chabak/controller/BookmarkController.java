package com.sikhye.chabak.controller;

import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.service.bookmark.BookmarkService;
import com.sikhye.chabak.service.bookmark.dto.BookmarkRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

	private final BookmarkService bookmarkService;

	public BookmarkController(BookmarkService bookmarkService) {
		this.bookmarkService = bookmarkService;
	}

	@GetMapping
	public BaseResponse<List<BookmarkRes>> findBookmark() {
		return new BaseResponse<>(bookmarkService.findBookmark());
	}

	@PostMapping("/{placeId}")
	public BaseResponse<Long> addBookmark(@PathVariable Long placeId) {
		return new BaseResponse<>(bookmarkService.registerBookmark(placeId));
	}

	@PatchMapping("/{bookmarkId}/status")
	public BaseResponse<Long> statusToDeleteBookmark(@PathVariable Long bookmarkId) {
		return new BaseResponse<>(bookmarkService.statusToDeleteBookmark(bookmarkId));
	}
}
