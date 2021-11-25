package com.sikhye.chabak.src.tag;

import com.sikhye.chabak.base.BaseResponse;
import com.sikhye.chabak.src.tag.dto.PlaceTagReq;
import com.sikhye.chabak.src.tag.dto.PlaceTagRes;
import com.sikhye.chabak.src.tag.dto.PostingTagReq;
import com.sikhye.chabak.src.tag.dto.PostingTagRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class TagController {

	private final TagService tagService;


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
