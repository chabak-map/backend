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
import com.sikhye.chabak.service.jwt.JwtTokenService;
import com.sikhye.chabak.service.place.dto.PlaceCommentReq;
import com.sikhye.chabak.service.place.dto.PlaceCommentRes;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceSearchRes;
import com.sikhye.chabak.service.place.dto.PlaceTagReq;
import com.sikhye.chabak.service.place.dto.PlaceTagRes;
import com.sikhye.chabak.service.place.entity.Place;
import com.sikhye.chabak.service.place.entity.PlaceComment;
import com.sikhye.chabak.service.place.entity.PlaceImage;
import com.sikhye.chabak.service.place.entity.PlaceTag;
import com.sikhye.chabak.service.place.repository.PlaceCommentRepository;
import com.sikhye.chabak.service.place.repository.PlaceImageRepository;
import com.sikhye.chabak.service.place.repository.PlaceRepository;
import com.sikhye.chabak.service.place.repository.PlaceTagRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService {

	private final PlaceRepository placeRepository;
	private final PlaceImageRepository placeImageRepository;
	private final PlaceCommentRepository placeCommentRepository;
	private final PlaceTagRepository placeTagRepository;
	private final JwtTokenService jwtTokenService;

	public PlaceServiceImpl(PlaceRepository placeRepository,
		PlaceImageRepository placeImageRepository,
		PlaceCommentRepository placeCommentRepository,
		PlaceTagRepository placeTagRepository, JwtTokenService jwtTokenService) {
		this.placeRepository = placeRepository;
		this.placeImageRepository = placeImageRepository;
		this.placeCommentRepository = placeCommentRepository;
		this.placeTagRepository = placeTagRepository;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public PlaceDetailRes getPlace(Long placeId) {

		// 장소/이미지/리뷰
		Place place = placeRepository.findPlaceByIdAndStatus(placeId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));
		Optional<List<PlaceImage>> placeImageResults = placeImageRepository.findPlaceImagesByPlaceIdAndStatus(placeId,
			USED);    // TODO: orElse 변경
		Optional<List<PlaceComment>> placeReviewResults = placeCommentRepository.findPlaceCommentsByPlaceIdAndStatus(
			placeId, USED);
		List<String> placeTagNames = findPlaceTags(place.getId());

		long imageCount = 0L;
		List<PlaceImage> placeImages = new ArrayList<>();
		if (placeImageResults.isPresent()) {
			placeImages = placeImageResults.get();
			imageCount = placeImages.size();
		}

		long reviewCount = 0L;
		List<PlaceComment> placeReviews = new ArrayList<>();
		if (placeReviewResults.isPresent()) {
			placeReviews = placeReviewResults.get();
			reviewCount = placeReviews.size();
		}

		return PlaceDetailRes.builder()
			.name(place.getName())
			.address(place.getAddress())
			.placeImageUrls(placeImages.stream().map(PlaceImage::getImageUrl).collect(Collectors.toList()))
			.commentResList(placeReviews.stream().map(placeReview -> {
				return PlaceCommentRes.builder()
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

	@Override
	public List<String> findPlaceTags(Long placeId) {
		List<PlaceTag> placeTags = placeTagRepository.findPlaceTagsByPlaceIdAndStatus(placeId, USED)
			.orElse(Collections.emptyList());

		return placeTags.stream()
			.map(PlaceTag::getName)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<PlaceTagRes> addPlaceTags(Long placeId, PlaceTagReq placeTagReq) {
		List<String> placeTagNames = placeTagReq.getPlaceTags();

		return placeTagNames.stream()
			.map(s -> {
				PlaceTag toSavePlaceTag = PlaceTag.builder()
					.name(s)
					.placeId(placeId)
					.build();

				PlaceTag savedPlaceTag = placeTagRepository.save(toSavePlaceTag);
				return new PlaceTagRes(savedPlaceTag.getId(), savedPlaceTag.getName());
			})
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Long editPlaceTag(Long placeId, Long placeTagId, String placeTagName) {
		PlaceTag findPlaceTag = placeTagRepository.findPlaceTagByIdAndStatus(placeTagId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		if (!findPlaceTag.getPlaceId().equals(placeId))
			throw new BaseException(SEARCH_NOT_FOUND_PLACE);

		findPlaceTag.setName(placeTagName);

		return placeTagId;
	}

	@Override
	@Transactional
	public Long placeTagStatusToDelete(Long placeId, Long placeTagId) {
		PlaceTag findPlaceTag = placeTagRepository.findPlaceTagByIdAndStatus(placeTagId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		if (!findPlaceTag.getPlaceId().equals(placeId))
			throw new BaseException(SEARCH_NOT_FOUND_PLACE);

		findPlaceTag.setStatusToDelete();

		return placeTagId;
	}

	@Override
	public List<PlaceCommentRes> findPlaceComments(Long placeId) {
		List<PlaceComment> placeReviews = placeCommentRepository.findPlaceCommentsByPlaceIdAndStatus(placeId, USED)
			.orElse(Collections.emptyList());

		return placeReviews.stream()
			.map(placeReview -> PlaceCommentRes.builder()
				.name(placeReview.getMember().getNickname())
				.content(placeReview.getContent())
				.writingDate(placeReview.getCreatedAt().toLocalDate())
				.build()).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Long addPlaceComment(Long placeId, PlaceCommentReq commentReq) {
		Long memberId = jwtTokenService.getMemberId();

		PlaceComment toSavePlaceReview = PlaceComment.builder()
			.placeId(placeId)
			.memberId(memberId)
			.content(commentReq.getContent())
			.build();

		return placeCommentRepository.save(toSavePlaceReview).getId();

	}

	@Override
	@Transactional
	public Long editPlaceComment(Long placeId, Long commentId, PlaceCommentReq commentReq) {
		Long memberId = jwtTokenService.getMemberId();

		PlaceComment findPlaceReview = placeCommentRepository.findPlaceCommentByIdAndStatus(commentId, USED);

		if (!memberId.equals(findPlaceReview.getMemberId())) {
			throw new BaseException(INVALID_USER_JWT);
		}

		findPlaceReview.setContent(commentReq.getContent());

		return findPlaceReview.getId();
	}

	@Override
	@Transactional
	public Long statusToDeletePlaceComment(Long placeId, Long commentId) {
		Long memberId = jwtTokenService.getMemberId();

		PlaceComment findPlaceReview = placeCommentRepository.findPlaceCommentByIdAndStatus(commentId, USED);

		if (!memberId.equals(findPlaceReview.getMemberId())) {
			throw new BaseException(INVALID_USER_JWT);
		} else if (!placeId.equals(findPlaceReview.getPlaceId())) {
			throw new BaseException(DELETE_EMPTY);
		} else {
			findPlaceReview.setStatusToDelete();

			return findPlaceReview.getId();
		}
	}

}

// .placeImages(placeImages.stream().map(PlaceImage::getImageUrl).collect(Collectors.toList()))
