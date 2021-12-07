package com.sikhye.chabak.service.bookmark;

import com.sikhye.chabak.service.bookmark.dto.BookmarkRes;

import java.util.List;

public interface BookmarkService {

	// 01. 북마크 조회
	List<BookmarkRes> findBookmark();

	// 02. 북마크 등록 (장소에 대한 북마크)
	Long registerBookmark(Long placeId);

	// 03. 북마크 삭제
	Long statusToDeleteBookmark(Long bookmarkId);

}
