package com.sikhye.chabak.src.place;

import com.sikhye.chabak.src.place.dto.PlaceDetailRes;
import com.sikhye.chabak.src.place.dto.PlaceRes;
import com.sikhye.chabak.src.place.dto.PlaceSearchRes;
import com.sikhye.chabak.src.place.entity.PlaceTag;

import java.util.List;

public interface PlaceService {

	// +. 장소 ID를 기준으로 태그 찾기
	List<PlaceTag> findTags(Long memberId);

	// 01. 장소 상세정보 API
	PlaceDetailRes getPlace(Long placeId);

	// 02. 장소 반경조회 API
	List<PlaceSearchRes> aroundPlace(Double latitude, Double longitude, Double radius);

	// 03. 장소 삭제 API
	Long statusToDelete(Long placeId);

	// 04. 장소 좌표 저장 API
	Long savePoint(Long placeId, Double latitude, Double longitude);




}
