package com.sikhye.chabak.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.service.place.PlaceService;
import com.sikhye.chabak.service.place.dto.PlaceCommentReq;
import com.sikhye.chabak.service.place.dto.PlaceCommentRes;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceSearchRes;
import com.sikhye.chabak.service.place.dto.PlaceTagReq;
import com.sikhye.chabak.service.place.dto.PlaceTagRes;

import lombok.extern.slf4j.Slf4j;

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

	@GetMapping("/places/{placeId}/tags")
	public BaseResponse<List<String>> getPlaceTags(@PathVariable Long placeId) {
		return new BaseResponse<>(placeService.findPlaceTags(placeId));
	}

	@PostMapping("/places/{placeId}/tags")
	public BaseResponse<List<PlaceTagRes>> addPlaceTags(@PathVariable Long placeId,
		@RequestBody PlaceTagReq placeTagReq) {
		return new BaseResponse<>(placeService.addPlaceTags(placeId, placeTagReq));
	}

	@PatchMapping("/places/{placeId}/tags/{tagId}")
	public BaseResponse<Long> editPlaceTag(@PathVariable Long placeId,
		@PathVariable Long tagId,
		@RequestParam String tagName) {
		return new BaseResponse<>(placeService.editPlaceTag(placeId, tagId, tagName));

	}

	@PatchMapping("/places/{placeId}/tags/{tagId}/status")
	public BaseResponse<Long> statusToDeletePlaceTag(@PathVariable Long placeId,
		@PathVariable Long tagId) {

		return new BaseResponse<>(placeService.placeTagStatusToDelete(placeId, tagId));
	}

	@GetMapping("/places/{placeId}/comments")
	public BaseResponse<List<PlaceCommentRes>> findPlaceComments(@PathVariable Long placeId) {
		return new BaseResponse<>(placeService.findPlaceComments(placeId));
	}

	@PostMapping("/places/{placeId}/comments")
	public BaseResponse<Long> addPlaceComment(@PathVariable Long placeId,
		@Valid @RequestBody PlaceCommentReq commentReq) {
		return new BaseResponse<>(placeService.addPlaceComment(placeId, commentReq));
	}

	@PatchMapping("/places/{placeId}/comments/{commentId}")
	public BaseResponse<Long> editPlaceComment(@PathVariable Long placeId, @PathVariable Long commentId,
		@Valid @RequestBody PlaceCommentReq commentReq) {
		return new BaseResponse<>(placeService.editPlaceComment(placeId, commentId, commentReq));
	}

	@PatchMapping("/places/{placeId}/comments/{commentId}/status")
	public BaseResponse<Long> statusToDeletePlaceComment(@PathVariable Long placeId, @PathVariable Long commentId) {
		return new BaseResponse<>(placeService.statusToDeletePlaceComment(placeId, commentId));
	}

}
