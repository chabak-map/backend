package com.sikhye.chabak.service.place;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sikhye.chabak.service.place.constant.SortType;
import com.sikhye.chabak.service.place.dto.PlaceAroundRes;
import com.sikhye.chabak.service.place.dto.PlaceCommentReq;
import com.sikhye.chabak.service.place.dto.PlaceCommentRes;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceRankRes;
import com.sikhye.chabak.service.place.dto.PlaceSearchRes;
import com.sikhye.chabak.service.place.dto.PlaceTagReq;
import com.sikhye.chabak.service.place.dto.PlaceTagRes;
import com.sikhye.chabak.service.place.entity.Place;
import com.sikhye.chabak.service.place.entity.PlaceTag;

public interface PlaceService {

	// 01. 장소 상세정보 API
	PlaceDetailRes getPlace(Long placeId, Double latitude, Double longitude);

	// 02. 장소 반경조회 API
	List<PlaceAroundRes> aroundPlace(Double latitude, Double longitude, Double radius);

	// 03. 장소 삭제 API
	Long statusToDelete(Long placeId);

	// 04. 장소 좌표 저장 API
	Long savePoint(Long placeId, Map<String, String> point);

	// 05. 장소 태그 조회 API
	List<String> findPlaceTags(Long placeId);

	// 06. 장소 태그 등록 API
	List<PlaceTagRes> addPlaceTags(Long placeId, PlaceTagReq placeTagReq);

	// 07. 장소 태그 수정 API
	Long editPlaceTag(Long placeId, Long placeTagId, String placeTagName);

	// 08. 장소 태그 삭제 API
	Long placeTagStatusToDelete(Long placeId, Long placeTagId);

	// 09. 장소 댓글 리스트 조회
	List<PlaceCommentRes> findPlaceComments(Long placeId);

	// 10. 장소 댓글 작성
	Long addPlaceComment(Long placeId, PlaceCommentReq commentReq);

	// 11. 장소 댓글 수정
	Long editPlaceComment(Long placeId, Long commentId, PlaceCommentReq commentReq);

	// 12. 장소 댓글 삭제
	Long statusToDeletePlaceComment(Long placeId, Long commentId);

	// 13. 장소명 검색
	List<PlaceSearchRes> searchPlacesOrder(String query, Double lat, Double lng, SortType sortType);

	// 13-1. 지역별 검색
	List<PlaceSearchRes> searchPlacesRegion(String query, Double lat, Double lng);

	// 14. 장소 랭킹
	List<PlaceRankRes> getTop5PlaceRanks();

	// // 20211216
	// // 15. 모든 장소 조회 ( ES 용도 )
	// List<Place> findPlaces();
	//
	// int countPlaces();
	//
	// // 16. 모든 장소 리스트 조회 ( ES 용도 )
	// List<PlaceImage> findPlaceImages();

	// 15. 검색 전용
	List<Place> searchPlacesBy(String keyword);

	List<PlaceTag> searchPlaceTagsBy(String tagName);

	Optional<Place> findBy(Long id);

}
