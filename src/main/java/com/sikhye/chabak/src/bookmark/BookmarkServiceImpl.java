package com.sikhye.chabak.src.bookmark;

import com.sikhye.chabak.base.exception.BaseException;
import com.sikhye.chabak.src.bookmark.dto.BookmarkRes;
import com.sikhye.chabak.src.bookmark.entity.Bookmark;
import com.sikhye.chabak.src.place.PlaceService;
import com.sikhye.chabak.src.place.dto.PlaceDetailRes;
import com.sikhye.chabak.utils.JwtService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sikhye.chabak.base.BaseResponseStatus.DELETE_EMPTY;
import static com.sikhye.chabak.base.BaseResponseStatus.NOT_TO_DELETE;
import static com.sikhye.chabak.base.entity.BaseStatus.used;
import static java.util.Collections.emptyList;

@Slf4j
@Service
@Transactional(readOnly = true)
public class BookmarkServiceImpl implements BookmarkService {

	private final PlaceService placeService;
	private final BookmarkRepository bookmarkRepository;
	private final JwtService jwtService;

	@Builder
	public BookmarkServiceImpl(PlaceService placeService, BookmarkRepository bookmarkRepository, JwtService jwtService) {
		this.placeService = placeService;
		this.bookmarkRepository = bookmarkRepository;
		this.jwtService = jwtService;
	}

	@Override
	public List<BookmarkRes> findBookmark() {
		// 멤버 ID 추출
		Long memberId = jwtService.getUserIdx();

		// 멤버 ID를 기준으로 북마크 조회
		List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByMemberIdAndStatus(memberId, used).orElse(emptyList());

		// List<Bookmark> :: Entity -> List<BookmarkRes> :: DTO 변환 및 반환
		return bookmarks.stream()
			.map(bookmark -> {
				PlaceDetailRes place = placeService.getPlace(bookmark.getPlaceId());
				return BookmarkRes.builder()
					.name(place.getName())    // TODO: 최적화 (getPlace 아닌 다른 함수 만들기)
					.address(place.getAddress())
					.imageUrl(place.getPlaceImageUrls().get(0)) // TODO: NPE 발생 가능 (이미지 미 존재 시)
					.placeTagNames(place.getTagNames())    // TODO: NPE 발생 가능
					.build();
			})
			.collect(Collectors.toList());

	}


	@Override
	@Transactional
	public Long registerBookmark(Long placeId) {
		// 멤버 ID 추출
		Long memberId = jwtService.getUserIdx();

		Bookmark newBookmark = Bookmark.builder()
			.memberId(memberId)
			.placeId(placeId)
			.build();
		return bookmarkRepository.save(newBookmark).getId();
	}

	@Override
	@Transactional
	public Long statusToDeleteBookmark(Long bookmarkId) {
		Long memberId = jwtService.getUserIdx();

		Bookmark findBookmark = bookmarkRepository.findBookmarkByIdAndStatus(bookmarkId, used)
			.orElseThrow(() -> new BaseException(DELETE_EMPTY));

		if (memberId.equals(findBookmark.getId())) {
			findBookmark.setStatusToDelete();
			return findBookmark.getId();
		} else {
			throw new BaseException(NOT_TO_DELETE);
		}
	}
}
