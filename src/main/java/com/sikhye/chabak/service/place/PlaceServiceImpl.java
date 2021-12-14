package com.sikhye.chabak.service.place;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.jwt.JwtTokenService;
import com.sikhye.chabak.service.member.MemberService;
import com.sikhye.chabak.service.member.entity.Member;
import com.sikhye.chabak.service.place.dto.PlaceAroundRes;
import com.sikhye.chabak.service.place.dto.PlaceCommentReq;
import com.sikhye.chabak.service.place.dto.PlaceCommentRes;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceImageRes;
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
	private final MemberService memberService;
	private final JwtTokenService jwtTokenService;

	public PlaceServiceImpl(PlaceRepository placeRepository,
		PlaceImageRepository placeImageRepository,
		PlaceCommentRepository placeCommentRepository,
		PlaceTagRepository placeTagRepository, MemberService memberService,
		JwtTokenService jwtTokenService) {
		this.placeRepository = placeRepository;
		this.placeImageRepository = placeImageRepository;
		this.placeCommentRepository = placeCommentRepository;
		this.placeTagRepository = placeTagRepository;
		this.memberService = memberService;
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
			.id(place.getId())
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
	public List<PlaceAroundRes> aroundPlace(Double latitude, Double longitude, Double radius) {
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

	/**
	 * 거리순 장소 검색
	 *
	 * @param query 검색어
	 * @param lat 현재 위도
	 * @param lng 현재 경도
	 * @return 거리순으로 나열된 장소 리스트
	 */
	@Override
	public List<PlaceSearchRes> searchPlacesDistanceOrder(String query, Double lat, Double lng) {

		Long memberId = jwtTokenService.getMemberId();

		Member findMember = memberService.findMemberBy(memberId)
			.orElseThrow(() -> new BaseException(CHECK_USER));

		// ptpt :: 비어있는 리스트로 반환 ( repository 결과가 없을 때 )
		// 1) 장소이름
		List<Place> places = placeRepository.findPlacesByNameLikeAndStatus(query, USED)
			.orElse(Collections.emptyList());

		// 2) 장소태그
		List<PlaceTag> placeTags = placeTagRepository.findPlaceTagsByNameLikeAndStatus(
			query, USED).orElse(Collections.emptyList());

		// 3) 장소 태그에서 중복된 placeId는 필터링 한다.
		List<Place> combinedPlaces = Stream.concat( // 5) 장소이름, 장소태그를 합친다.
				placeTags.stream()
					.map(PlaceTag::getPlaceId)
					.distinct()
					.map((placeId) -> placeRepository.findPlaceByIdAndStatus(placeId, USED)
						.orElse(null)), // 4) 장소태그의 placeId를 가지고 장소이름을 검색한다.
				places.stream())
			.collect(Collectors.toList());

		// 6) PlaceSearchRes로 변환한다.
		List<PlaceSearchRes> searchPlaces = combinedPlaces.stream()
			.map(place ->
				PlaceSearchRes.builder()
					.name(place.getName())
					.address(place.getAddress())
					.reviewCount(placeCommentRepository.countByPlaceIdAndStatus(place.getId(), USED))
					.distance(getDistance(lat, lng, place.getLatitude(), place.getLongitude()))
					.placeTags(place.getTags().stream().map(placeTag -> new PlaceTagRes(placeTag.getPlaceId(),
						placeTag.getName())).collect(Collectors.toList()))
					.placeImages(place.getPlaceImages().stream()
						.map(placeImage -> PlaceImageRes.builder()
							.imageId(placeImage.getId())
							.imageUrl(placeImage.getImageUrl())
							.build())
						.collect(Collectors.toList()))
					.isBookmarked(findMember.getBookmarks()
						.stream()
						.anyMatch(bookmark -> bookmark.getPlaceId().equals(place.getId())))
					.build())
			.sorted(Comparator.comparingDouble(PlaceSearchRes::getDistance)) // 7)  sorting
			.collect(Collectors.toList());

		return searchPlaces;
	}

	@Override
	public List<PlaceSearchRes> searchPlacesRelateOrder(String query, Double lat, Double lng) {
		return null;
	}

	// ====================================================================
	// INTERNAL USE
	// ====================================================================

	/**
	 * 두 지점간 거리 계산
	 *
	 * @param srcLat 시작지 위도
	 * @param srcLng 시작지 경도
	 * @param dstLat 목적지 위도
	 * @param dstLng 목적지 경도
	 * @return km단위 거리
	 */
	private double getDistance(double srcLat, double srcLng, double dstLat, double dstLng) {

		// 소수점 반올림 설정
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(3);
		nf.setGroupingUsed(false);    // 지수표현 제거

		double theta = srcLng - dstLng;
		double dist =
			Math.sin(deg2rad(srcLat)) * Math.sin(deg2rad(dstLat)) + Math.cos(deg2rad(srcLat)) * Math.cos(
				deg2rad(dstLat))
				* Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1.609344;

		return Double.parseDouble(nf.format(dist));
	}

	// This function converts decimal degrees to radians
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// This function converts radians to decimal degrees
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}

// .placeImages(placeImages.stream().map(PlaceImage::getImageUrl).collect(Collectors.toList()))
