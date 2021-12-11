package com.sikhye.chabak.service.place;

import java.util.List;

import com.sikhye.chabak.service.place.dto.PlaceCommentReq;
import com.sikhye.chabak.service.place.dto.PlaceCommentRes;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceSearchRes;
import com.sikhye.chabak.service.place.dto.PlaceTagReq;
import com.sikhye.chabak.service.place.dto.PlaceTagRes;

public interface PlaceService {

	// 01. 장소 상세정보 API
	PlaceDetailRes getPlace(Long placeId);

	// 02. 장소 반경조회 API
	List<PlaceSearchRes> aroundPlace(Double latitude, Double longitude, Double radius);

	// 03. 장소 삭제 API
	Long statusToDelete(Long placeId);

	// 04. 장소 좌표 저장 API
	Long savePoint(Long placeId, Double latitude, Double longitude);

	// 장소 태그 조회 API
	List<String> findPlaceTags(Long placeId);

	// 장소 태그 등록 API
	List<PlaceTagRes> addPlaceTags(Long placeId, PlaceTagReq placeTagReq);

	// 장소 태그 수정 API
	Long editPlaceTag(Long placeId, Long placeTagId, String placeTagName);

	// 장소 태그 삭제 API
	Long placeTagStatusToDelete(Long placeId, Long placeTagId);

	// 01. 장소 댓글 리스트 조회
	List<PlaceCommentRes> findPlaceComments(Long placeId);

	// 02. 장소 댓글 작성
	Long addPlaceComment(Long placeId, PlaceCommentReq commentReq);

	// 03. 장소 댓글 수정
	Long editPlaceComment(Long placeId, Long commentId, PlaceCommentReq commentReq);

	// 04. 장소 댓글 삭제
	Long statusToDeletePlaceComment(Long placeId, Long commentId);

}
