package com.sikhye.chabak.service.place;

import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceSearchRes;

import java.util.List;

public interface PlaceService {

	// 01. 장소 상세정보 API
	PlaceDetailRes getPlace(Long placeId);

	// 02. 장소 반경조회 API
	List<PlaceSearchRes> aroundPlace(Double latitude, Double longitude, Double radius);

	// 03. 장소 삭제 API
	Long statusToDelete(Long placeId);

	// 04. 장소 좌표 저장 API
	Long savePoint(Long placeId, Double latitude, Double longitude);


}
