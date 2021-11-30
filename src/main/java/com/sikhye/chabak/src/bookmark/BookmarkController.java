package com.sikhye.chabak.src.bookmark;

import com.sikhye.chabak.base.BaseResponse;
import com.sikhye.chabak.src.bookmark.dto.BookmarkRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

	private final BookmarkService bookmarkService;

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
