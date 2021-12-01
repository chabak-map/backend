package com.sikhye.chabak.src.place;

import com.sikhye.chabak.base.BaseResponse;
import com.sikhye.chabak.src.place.dto.PlaceDetailRes;
import com.sikhye.chabak.src.place.dto.PlaceSearchRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/places")
public class PlaceController {

	private final PlaceService placeService;

	public PlaceController(PlaceService placeService) {
		this.placeService = placeService;
	}

	// 장소 상세정보
	@GetMapping("/{placeId}")
	public BaseResponse<PlaceDetailRes> getPlaceDetail(@PathVariable Long placeId) {
		return new BaseResponse<>(placeService.getPlace(placeId));
	}

	// 주변 차박지 찾기
	@GetMapping
	public BaseResponse<List<PlaceSearchRes>> getAroundPlace(@RequestParam("lat") Double latitude,
															 @RequestParam("lng") Double longitude,
															 @RequestParam("r") Double radius) {
		return new BaseResponse<>(placeService.aroundPlace(latitude, longitude, radius));
	}

	// 장소 삭제
	// TODO: 좌표값은 관리자만 설정할 수 있도록 변경 (JWT 토큰 이용)
	@PatchMapping("/{placeId}/status")
	public BaseResponse<Long> deletePlace(@PathVariable Long placeId) {
		return new BaseResponse<>(placeService.statusToDelete(placeId));
	}

	// 장소 좌표 저장
	// TODO: 좌표값은 관리자만 설정할 수 있도록 변경 (JWT 토큰 이용)
	@PostMapping("/{placeId}")
	public BaseResponse<Long> savePoint(@PathVariable Long placeId,
										@RequestParam("lat") Double latitude,
										@RequestParam("lng") Double longitude) {
		return new BaseResponse<>(placeService.savePoint(placeId, latitude, longitude));
	}


}
