package com.sikhye.chabak.src.place;

import com.sikhye.chabak.base.exception.BaseException;
import com.sikhye.chabak.src.comment.dto.CommentRes;
import com.sikhye.chabak.src.comment.entity.PlaceReview;
import com.sikhye.chabak.src.comment.repository.PlaceReviewRepository;
import com.sikhye.chabak.src.place.dto.PlaceDetailRes;
import com.sikhye.chabak.src.place.dto.PlaceSearchRes;
import com.sikhye.chabak.src.place.entity.Place;
import com.sikhye.chabak.src.place.entity.PlaceImage;
import com.sikhye.chabak.src.place.repository.PlaceImageRepository;
import com.sikhye.chabak.src.place.repository.PlaceRepository;
import com.sikhye.chabak.src.tag.TagService;
import com.sikhye.chabak.utils.JwtService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sikhye.chabak.base.BaseResponseStatus.DELETE_EMPTY;
import static com.sikhye.chabak.base.BaseResponseStatus.SEARCH_NOT_FOUND_PLACE;
import static com.sikhye.chabak.base.entity.BaseStatus.used;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService {

	private final PlaceRepository placeRepository;
	private final PlaceImageRepository placeImageRepository;
	private final PlaceReviewRepository placeReviewRepository;
	private final TagService tagService;
	private final JwtService jwtService;

	@Builder
	public PlaceServiceImpl(PlaceRepository placeRepository, PlaceImageRepository placeImageRepository, PlaceReviewRepository placeReviewRepository, TagService tagService, JwtService jwtService) {
		this.placeRepository = placeRepository;
		this.placeImageRepository = placeImageRepository;
		this.placeReviewRepository = placeReviewRepository;
		this.tagService = tagService;
		this.jwtService = jwtService;
	}

	@Override
	public PlaceDetailRes getPlace(Long placeId) {

		// 장소/이미지/리뷰
		Place place = placeRepository.findPlaceByIdAndStatus(placeId, used).orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));
		Optional<List<PlaceImage>> placeImageResults = placeImageRepository.findPlaceImagesByPlaceIdAndStatus(placeId, used);    // TODO: orElse 변경
		Optional<List<PlaceReview>> placeReviewResults = placeReviewRepository.findPlaceReviewsByPlaceIdAndStatus(placeId, used);
		List<String> placeTagNames = tagService.findPlaceTags(place.getId());


		long imageCount = 0L;
		List<PlaceImage> placeImages = new ArrayList<>();
		if (placeImageResults.isPresent()) {
			placeImages = placeImageResults.get();
			imageCount = placeImages.size();
		}

		long reviewCount = 0L;
		List<PlaceReview> placeReviews = new ArrayList<>();
		if (placeReviewResults.isPresent()) {
			placeReviews = placeReviewResults.get();
			reviewCount = placeReviews.size();
		}

		return PlaceDetailRes.builder()
			.name(place.getName())
			.address(place.getAddress())
			.placeImageUrls(placeImages.stream().map(PlaceImage::getImageUrl).collect(Collectors.toList()))
			.commentResList(placeReviews.stream().map(placeReview -> {
				return CommentRes.builder()
					.name(placeReview.getMember().getNickname())
					.content(placeReview.getContent())
					.writingDate(placeReview.getCreatedAt().toLocalDate())
					.build();
			}).collect(Collectors.toList()))
			.phoneNumber(place.getPhoneNumber())    // TODO: 복호화 필요
			.reviewCount(reviewCount)
			.imageCount(imageCount)
			.tagNames(placeTagNames)
			.build();
	}

	@Override
	public List<PlaceSearchRes> aroundPlace(Double latitude, Double longitude, Double radius) {
		return placeRepository.findPlaceNearbyPoint(latitude, longitude, radius).orElse(Collections.emptyList());
	}

	@Override
	@Transactional
	public Long statusToDelete(Long placeId) {
		Place findPlace = placeRepository.findPlaceByIdAndStatus(placeId, used).orElseThrow(() -> new BaseException(DELETE_EMPTY));

		findPlace.setStatusToDelete();
		return findPlace.getId();
	}

	@Override
	@Transactional
	public Long savePoint(Long placeId, Double latitude, Double longitude) {

		Place findPlace = placeRepository.findPlaceByIdAndStatus(placeId, used).orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));
		findPlace.setPoint(latitude, longitude);

		return findPlace.getId();
	}
}


// .placeImages(placeImages.stream().map(PlaceImage::getImageUrl).collect(Collectors.toList()))
