package com.sikhye.chabak.service.place;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static com.sikhye.chabak.service.place.constant.SortType.*;
import static java.util.Objects.*;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.global.exception.ExceptionFunction;
import com.sikhye.chabak.service.jwt.JwtTokenService;
import com.sikhye.chabak.service.member.MemberService;
import com.sikhye.chabak.service.member.domain.Member;
import com.sikhye.chabak.service.place.constant.SortType;
import com.sikhye.chabak.service.place.domain.DistrictRepository;
import com.sikhye.chabak.service.place.domain.Place;
import com.sikhye.chabak.service.place.domain.PlaceComment;
import com.sikhye.chabak.service.place.domain.PlaceCommentRepository;
import com.sikhye.chabak.service.place.domain.PlaceImage;
import com.sikhye.chabak.service.place.domain.PlaceImageRepository;
import com.sikhye.chabak.service.place.domain.PlaceRepository;
import com.sikhye.chabak.service.place.domain.PlaceTag;
import com.sikhye.chabak.service.place.domain.PlaceTagRepository;
import com.sikhye.chabak.service.place.dto.PlaceAroundRes;
import com.sikhye.chabak.service.place.dto.PlaceCommentReq;
import com.sikhye.chabak.service.place.dto.PlaceCommentRes;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceImageRes;
import com.sikhye.chabak.service.place.dto.PlaceRankRes;
import com.sikhye.chabak.service.place.dto.PlaceSearchRes;
import com.sikhye.chabak.service.place.dto.PlaceTagReq;
import com.sikhye.chabak.service.place.dto.PlaceTagRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService {

	private final PlaceRepository placeRepository;
	private final PlaceImageRepository placeImageRepository;
	private final PlaceCommentRepository placeCommentRepository;
	private final PlaceTagRepository placeTagRepository;
	private final DistrictRepository districtRepository;
	private final MemberService memberService;
	private final RedisTemplate<String, String> redisTemplate;
	private final JwtTokenService jwtTokenService;

	private final String ZSET_KEY = "views";

	public PlaceServiceImpl(PlaceRepository placeRepository,
		PlaceImageRepository placeImageRepository,
		PlaceCommentRepository placeCommentRepository,
		PlaceTagRepository placeTagRepository,
		DistrictRepository districtRepository, MemberService memberService,
		RedisTemplate<String, String> redisTemplate, JwtTokenService jwtTokenService) {
		this.placeRepository = placeRepository;
		this.placeImageRepository = placeImageRepository;
		this.placeCommentRepository = placeCommentRepository;
		this.placeTagRepository = placeTagRepository;
		this.districtRepository = districtRepository;
		this.memberService = memberService;
		this.redisTemplate = redisTemplate;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public PlaceDetailRes getPlace(Long placeId, Double latitude, Double longitude) {

		// ??????/?????????/??????
		Place place = placeRepository.findPlaceByIdAndStatus(placeId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));
		Optional<List<PlaceImage>> placeImageResults = placeImageRepository.findPlaceImagesByPlaceIdAndStatus(placeId,
			USED);
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

		PlaceDetailRes placeDetail = PlaceDetailRes.builder()
			.id(place.getId())
			.name(place.getName())
			.address(place.getAddress())
			.placeImageUrls(placeImages.stream().map(PlaceImage::getImageUrl).collect(Collectors.toList()))
			.commentResList(placeReviews.stream()
				.map(placeReview ->
					PlaceCommentRes.builder()
						.name(placeReview.getMember().getNickname())
						.content(placeReview.getContent())
						.writingDate(placeReview.getCreatedAt().toLocalDate())
						.build())
				.collect(Collectors.toList()))
			.phoneNumber(place.getPhoneNumber())
			.distance(getDistance(latitude, longitude, place.getLatitude(), place.getLongitude()))
			.url(place.getUrl())
			.reviewCount(reviewCount)
			.imageCount(imageCount)
			.tagNames(placeTagNames)
			.build();

		//?????? ??? ??????
		redisTemplate.opsForZSet().incrementScore(ZSET_KEY, Long.toString(place.getId()), 1);

		return placeDetail;
	}

	@Override
	public List<PlaceAroundRes> aroundPlace(Double latitude, Double longitude, Double radius) {
		return placeRepository.findPlaceNearbyPoint(latitude, longitude, radius).orElseGet(Collections::emptyList);
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
	public Long savePoint(Long placeId, Map<String, String> point) {

		Double latitude = Double.parseDouble(point.get("lat"));
		Double longitude = Double.parseDouble(point.get("lng"));
		String code = point.get("code");

		// 0?????? 5 ??????
		String clearedCode = code.substring(0, 5) + "00000";

		Place findPlace = placeRepository.findPlaceByIdAndStatus(placeId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));
		findPlace.setPoint(latitude, longitude, clearedCode);

		return findPlace.getId();
	}

	@Override
	public List<String> findPlaceTags(Long placeId) {
		List<PlaceTag> placeTags = placeTagRepository.findPlaceTagsByPlaceIdAndStatus(placeId, USED)
			.orElseGet(Collections::emptyList);

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
			.orElseGet(Collections::emptyList);

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
	 * ?????????/????????? ?????? ??????
	 *
	 * @param query ?????????
	 * @param lat   ?????? ??????
	 * @param lng   ?????? ??????
	 * @param sortType ????????????(?????????/?????????)
	 * @return ?????????/??????????????? ????????? ?????? ?????????
	 */
	@Override
	public List<PlaceSearchRes> searchPlacesOrder(String query, Double lat, Double lng, SortType sortType) {

		Long memberId = jwtTokenService.getMemberId();

		Member findMember = memberService.findMemberBy(memberId)
			.orElseThrow(() -> new BaseException(CHECK_USER));

		List<Place> places = placeRepository.findPlacesByNameContainingOrAddressContainingAndStatus(query, query, USED)
			.orElseGet(Collections::emptyList);

		Stream<PlaceSearchRes> placeSearchResStream = placesToSearchDTOs(places, findMember, lat, lng);

		if (sortType.equals(DISTANCE)) {
			return placeSearchResStream
				.sorted(Comparator.comparingDouble(PlaceSearchRes::getDistance))
				.collect(Collectors.toList());
		} else {
			// >> ptpt: ????????? ?????? ?????? ????????? ?????? ?????? ?????? ??? Collections??? ????????? ??????.
			return placeSearchResStream
				.sorted(Collections.reverseOrder(Comparator.comparingInt(
					(placeSearchRes) -> requireNonNullElse(
						redisTemplate.opsForZSet().score(ZSET_KEY, placeSearchRes.getId().toString()), 0.0).intValue()
				)))
				.collect(Collectors.toList());
		}

	}

	@Override
	public List<PlaceSearchRes> searchPlacesRegion(String query, Double lat, Double lng) {

		// 0) ?????? ?????? : '?????????-????????????*????????????'
		Long memberId = jwtTokenService.getMemberId();

		Member findMember = memberService.findMemberBy(memberId)
			.orElseThrow(() -> new BaseException(CHECK_USER));

		// 1) ?????? ?????? ( ????????? ????????? ???????????? ??? ?????? )
		String[] splitQuery = query.split("-");
		String region1Depth = splitQuery[0];

		List<String> region2Depths = new ArrayList<>();
		if (splitQuery[1].contains("*")) {    // 2??? ?????? ??????
			region2Depths = List.of(splitQuery[1].split("\\*"));
		} else {    // 1?????? ??????
			region2Depths.add(splitQuery[1]);
		}

		List<String> codeList = region2Depths.stream()
			.map(wrap(region2Depth -> (districtRepository.findByRegion1DepthContainingAndRegion2DepthContaining(
				region1Depth, region2Depth))
				.orElseThrow(() -> new BaseException(INVALID_DISTRICT_CODE)).getCode()))
			.collect(Collectors.toList());

		// 2) ????????? ???????????? ?????? ??????
		List<Place> findPlaces = codeList.stream()
			.map(
				code -> placeRepository.findPlacesByDistrictCodeAndStatus(code, USED)
					.orElseThrow(() -> new BaseException(INVALID_DISTRICT_CODE))
			)
			.flatMap(places -> places.stream())
			.collect(Collectors.toList());

		Stream<PlaceSearchRes> placeSearchResStream = placesToSearchDTOs(findPlaces, findMember, lat, lng);

		return placeSearchResStream.collect(Collectors.toList());
	}

	@Override
	public List<PlaceRankRes> getTop5PlaceRanks() {

		Set<ZSetOperations.TypedTuple<String>> rankSet = redisTemplate.opsForZSet()
			.reverseRangeWithScores(ZSET_KEY, 0, 4);

		if (rankSet == null || rankSet.isEmpty()) {
			return Collections.emptyList();
		}

		return rankSet.stream()
			.map(rank -> {
					Long placeId = Long.parseLong(requireNonNull(rank.getValue()));
					Place findPlace = placeRepository.findPlaceByIdAndStatus(placeId, USED)
						.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

					return PlaceRankRes.builder()
						.viewCount(requireNonNull(rank.getScore()).intValue())
						.placeId(placeId)
						.name(findPlace.getName())
						.address(findPlace.getAddress())
						.placeImageUrl(
							findPlace.getPlaceImages()
								.stream()
								.limit(1)
								.map(PlaceImage::getImageUrl)
								.collect(Collectors.joining()))
						.build();
				}
			).collect(Collectors.toList());
	}

	@Override
	public List<Place> searchPlacesBy(String keyword) {
		return placeRepository.findByNameContainsAndStatus(keyword, USED).orElseGet(Collections::emptyList);
	}

	@Override
	public List<PlaceTag> searchPlaceTagsBy(String tagName) {
		return placeTagRepository.findByNameAndStatus(tagName, USED).orElseGet(Collections::emptyList);
	}

	@Override
	public Optional<Place> findBy(Long id) {
		return placeRepository.findPlaceByIdAndStatus(id, USED);
	}

	// // 20211216
	// @Override
	// public List<Place> findPlaces() {
	// 	// return placeRepository.findPlaceAllByUpdatedAtAfterAndStatus(LocalDateTime.now(), USED)
	// 	// 	.orElseGet(Collections::emptyList);
	//
	// 	// return placeRepository.findPlaceAllByUpdatedAtAfterAndStatus(
	// 	// 		LocalDateTime.of(2021, Month.JANUARY, 1, 1, 11, 11),
	// 	// 		USED)
	// 	// 	.orElseGet(Collections::emptyList);
	//
	// 	return placeRepository.findPlaceAllByUpdatedAtAfterAndStatus(
	// 			LocalDateTime.of(2022, Month.JANUARY, 8, 1, 11, 11),
	// 			USED)
	// 		.orElseGet(Collections::emptyList);
	// }
	//
	// @Override
	// public int countPlaces() {
	// 	return placeRepository.countAllByStatus(USED);
	// }
	//
	// @Override
	// public List<PlaceImage> findPlaceImages() {
	// 	return placeImageRepository.findPlaceImageAllByStatus(USED).orElseGet(Collections::emptyList);
	// }

	// ====================================================================
	// INTERNAL USE
	// ====================================================================

	// >> ptpt: stream????????? ????????? ?????????????????? ?????? exception ?????? (?????????)
	private static <T, R> Function<T, R> wrap(ExceptionFunction<T, R> f) {
		return (T t) -> {
			try {
				return f.apply(t);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BaseException(SEARCHED_DUPLICATE_REGION);
			}
		};
	}

	private Stream<PlaceSearchRes> placesToSearchDTOs(List<Place> placeList, Member member, Double lat, Double
		lng) {

		return placeList.stream()
			.map(place ->
				PlaceSearchRes.builder()
					.id(place.getId())
					.name(place.getName())
					.address(place.getAddress())
					.reviewCount(placeCommentRepository.countByPlaceIdAndStatus(place.getId(), USED))
					.distance(getDistance(lat, lng, place.getLatitude(), place.getLongitude()))
					.placeTags(place.getTags().stream().map(placeTag -> new PlaceTagRes(placeTag.getId(),
						placeTag.getName())).collect(Collectors.toList()))
					.placeImages(place.getPlaceImages().stream()
						.map(placeImage -> PlaceImageRes.builder()
							.imageId(placeImage.getId())
							.imageUrl(placeImage.getImageUrl())
							.build())
						.collect(Collectors.toList()))
					.isBookmarked(member.getBookmarks()
						.stream()
						.anyMatch(bookmark -> bookmark.getPlaceId().equals(place.getId())))
					.build());
	}

	/**
	 * ??? ????????? ?????? ??????
	 *
	 * @param srcLat ????????? ??????
	 * @param srcLng ????????? ??????
	 * @param dstLat ????????? ??????
	 * @param dstLng ????????? ??????
	 * @return km?????? ??????
	 */
	private Double getDistance(Double srcLat, Double srcLng, Double dstLat, Double dstLng) {

		if (srcLat == null || srcLng == null) {
			return null;
		}

		// ????????? ????????? ??????
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(3);
		nf.setGroupingUsed(false);    // ???????????? ??????

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
