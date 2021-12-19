package com.sikhye.chabak.service.bookmark;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.bookmark.domain.Bookmark;
import com.sikhye.chabak.service.bookmark.domain.BookmarkRepository;
import com.sikhye.chabak.service.bookmark.dto.BookmarkRes;
import com.sikhye.chabak.service.jwt.JwtTokenService;
import com.sikhye.chabak.service.place.PlaceService;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class BookmarkServiceImpl implements BookmarkService {

	private final PlaceService placeService;
	private final BookmarkRepository bookmarkRepository;
	private final JwtTokenService jwtTokenService;

	@Builder
	public BookmarkServiceImpl(PlaceService placeService, BookmarkRepository bookmarkRepository,
		JwtTokenService jwtTokenService) {
		this.placeService = placeService;
		this.bookmarkRepository = bookmarkRepository;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public List<BookmarkRes> findBookmark() {
		// 멤버 ID 추출
		Long memberId = jwtTokenService.getMemberId();

		// 멤버 ID를 기준으로 북마크 조회
		List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByMemberIdAndStatus(memberId, USED)
			.orElseGet(Collections::emptyList);

		// List<Bookmark> :: Entity -> List<BookmarkRes> :: DTO 변환 및 반환
		return bookmarks.stream()
			.map(bookmark -> {
				PlaceDetailRes place = placeService.getPlace(bookmark.getPlaceId(), null, null);
				return BookmarkRes.builder()
					.id(place.getId())
					.name(place.getName())
					.address(place.getAddress())
					.imageUrl(place.getPlaceImageUrls().isEmpty() ? null :
						place.getPlaceImageUrls().get(0))
					.placeTagNames(place.getTagNames())
					.build();
			})
			.collect(Collectors.toList());

	}

	@Override
	@Transactional
	public Long registerBookmark(Long placeId) {
		// 멤버 ID 추출
		Long memberId = jwtTokenService.getMemberId();

		Bookmark newBookmark = Bookmark.builder()
			.memberId(memberId)
			.placeId(placeId)
			.build();
		return bookmarkRepository.save(newBookmark).getId();
	}

	@Override
	@Transactional
	public Long statusToDeleteBookmark(Long bookmarkId) {
		Long memberId = jwtTokenService.getMemberId();

		Bookmark findBookmark = bookmarkRepository.findBookmarkByIdAndStatus(bookmarkId, USED)
			.orElseThrow(() -> new BaseException(DELETE_EMPTY));

		if (memberId.equals(findBookmark.getMemberId())) {
			findBookmark.setStatusToDelete();
			return findBookmark.getId();
		} else {
			throw new BaseException(NOT_TO_DELETE);
		}
	}
}
