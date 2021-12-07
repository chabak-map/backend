package com.sikhye.chabak.service.place;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.comment.dto.CommentRes;
import com.sikhye.chabak.service.comment.entity.PlaceReview;
import com.sikhye.chabak.service.comment.repository.PlaceReviewRepository;
import com.sikhye.chabak.service.jwt.JwtTokenService;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceSearchRes;
import com.sikhye.chabak.service.place.entity.Place;
import com.sikhye.chabak.service.place.entity.PlaceImage;
import com.sikhye.chabak.service.place.repository.PlaceImageRepository;
import com.sikhye.chabak.service.place.repository.PlaceRepository;
import com.sikhye.chabak.service.tag.TagService;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService {

	private final PlaceRepository placeRepository;
	private final PlaceImageRepository placeImageRepository;
	private final PlaceReviewRepository placeReviewRepository;
	private final TagService tagService;
	private final JwtTokenService jwtTokenService;

	@Builder
	public PlaceServiceImpl(PlaceRepository placeRepository, PlaceImageRepository placeImageRepository,
		PlaceReviewRepository placeReviewRepository, TagService tagService, JwtTokenService jwtTokenService) {
		this.placeRepository = placeRepository;
		this.placeImageRepository = placeImageRepository;
		this.placeReviewRepository = placeReviewRepository;
		this.tagService = tagService;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public PlaceDetailRes getPlace(Long placeId) {

		// 장소/이미지/리뷰
		Place place = placeRepository.findPlaceByIdAndStatus(placeId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));
		Optional<List<PlaceImage>> placeImageResults = placeImageRepository.findPlaceImagesByPlaceIdAndStatus(placeId,
			USED);    // TODO: orElse 변경
		Optional<List<PlaceReview>> placeReviewResults = placeReviewRepository.findPlaceReviewsByPlaceIdAndStatus(
			placeId, USED);
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
		Place findPlace = placeRepository.findPlaceByIdAndStatus(placeId, USED)
			.orElseThrow(() -> new BaseException(DELETE_EMPTY));

		findPlace.setStatusToDelete();
		return findPlace.getId();
	}

	@Override
	@Transactional
	public Long savePoint(Long placeId, Double latitude, Double longitude) {

		Place findPlace = placeRepository.findPlaceByIdAndStatus(placeId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));
		findPlace.setPoint(latitude, longitude);

		return findPlace.getId();
	}
}

// .placeImages(placeImages.stream().map(PlaceImage::getImageUrl).collect(Collectors.toList()))
