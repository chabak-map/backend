package com.sikhye.chabak.src.bookmark;

import com.sikhye.chabak.base.BaseResponse;
import com.sikhye.chabak.src.bookmark.dto.BookmarkRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
