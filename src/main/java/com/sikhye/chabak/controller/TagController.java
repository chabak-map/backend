package com.sikhye.chabak.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.service.tag.TagService;
import com.sikhye.chabak.service.tag.dto.PlaceTagReq;
import com.sikhye.chabak.service.tag.dto.PlaceTagRes;
import com.sikhye.chabak.service.tag.dto.PostingTagReq;
import com.sikhye.chabak.service.tag.dto.PostingTagRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
public class TagController {

	private final TagService tagService;

	public TagController(TagService tagService) {
		this.tagService = tagService;
	}

	// ======================================
	// 					장소
	// ======================================
	@GetMapping("/places/{placeId}/tags")
	public BaseResponse<List<String>> getPlaceTags(@PathVariable Long placeId) {
		return new BaseResponse<>(tagService.findPlaceTags(placeId));
	}

	@PostMapping("/places/{placeId}/tags")
	public BaseResponse<List<PlaceTagRes>> addPlaceTags(@PathVariable Long placeId,
		@RequestBody PlaceTagReq placeTagReq) {
		return new BaseResponse<>(tagService.addPlaceTags(placeId, placeTagReq));
	}

	@PatchMapping("/places/{placeId}/tags/{tagId}")
	public BaseResponse<Long> editPlaceTag(@PathVariable Long placeId,
		@PathVariable Long tagId,
		@RequestParam String tagName) {
		return new BaseResponse<>(tagService.editPlaceTag(placeId, tagId, tagName));

	}

	@PatchMapping("/places/{placeId}/tags/{tagId}/status")
	public BaseResponse<Long> statusToDeletePlaceTag(@PathVariable Long placeId,
		@PathVariable Long tagId) {

		return new BaseResponse<>(tagService.placeTagStatusToDelete(placeId, tagId));
	}

	// ======================================
	// 					포스팅
	// ======================================
	@GetMapping("/posts/{postId}/tags")
	public BaseResponse<List<String>> getPostingTags(@PathVariable Long postId) {
		return new BaseResponse<>(tagService.findPostingTags(postId));
	}

	@PostMapping("/posts/{postId}/tags")
	public BaseResponse<List<PostingTagRes>> addPostingTags(@PathVariable Long postId,
		@RequestBody PostingTagReq postingTagReq) {
		return new BaseResponse<>(tagService.addPostingTags(postId, postingTagReq));
	}

	@PatchMapping("/posts/{postId}/tags/{tagId}")
	public BaseResponse<Long> editPostingTag(@PathVariable Long postId,
		@PathVariable Long tagId,
		@RequestParam String tagName) {
		return new BaseResponse<>(tagService.editPostingTag(postId, tagId, tagName));
	}

	@PatchMapping("/posts/{postId}/tags/{tagId}/status")
	public BaseResponse<Long> statusToDeletePostingTag(@PathVariable Long postId,
		@PathVariable Long tagId) {
		return new BaseResponse<>(tagService.postingTagStatusToDelete(postId, tagId));
	}

}
