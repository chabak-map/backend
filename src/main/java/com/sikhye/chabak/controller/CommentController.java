package com.sikhye.chabak.controller;

import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.service.comment.CommentService;
import com.sikhye.chabak.service.comment.dto.CommentReq;
import com.sikhye.chabak.service.comment.dto.CommentRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class CommentController {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping("/places/{placeId}/comments")
	public BaseResponse<List<CommentRes>> findPlaceComments(@PathVariable Long placeId) {
		return new BaseResponse<>(commentService.findPlaceComments(placeId));
	}

	@PostMapping("/places/{placeId}/comments")
	public BaseResponse<Long> addPlaceComment(@PathVariable Long placeId,
											  @Valid @RequestBody CommentReq commentReq) {
		return new BaseResponse<>(commentService.addPlaceComment(placeId, commentReq));
	}

	@PatchMapping("/places/{placeId}/comments/{commentId}")
	public BaseResponse<Long> editPlaceComment(@PathVariable Long placeId, @PathVariable Long commentId,
											   @Valid @RequestBody CommentReq commentReq) {
		return new BaseResponse<>(commentService.editPlaceComment(placeId, commentId, commentReq));
	}

	@PatchMapping("/places/{placeId}/comments/{commentId}/status")
	public BaseResponse<Long> statusToDeletePlaceComment(@PathVariable Long placeId, @PathVariable Long commentId) {
		return new BaseResponse<>(commentService.statusToDeletePlaceComment(placeId, commentId));
	}


	@GetMapping("/posts/{postId}/comments")
	public BaseResponse<List<CommentRes>> findPostComments(@PathVariable Long postId) {
		return new BaseResponse<>(commentService.findPostComments(postId));
	}

	@PostMapping("/posts/{postId}/comments")
	public BaseResponse<Long> addPostComment(@PathVariable Long postId,
											 @Valid @RequestBody CommentReq commentReq) {
		return new BaseResponse<>(commentService.addPostComment(postId, commentReq));
	}

	@PatchMapping("/posts/{postId}/comments/{commentId}")
	public BaseResponse<Long> editPostComment(@PathVariable Long postId, @PathVariable Long commentId,
											  @Valid @RequestBody CommentReq commentReq) {
		return new BaseResponse<>(commentService.editPostComment(postId, commentId, commentReq));
	}

	@PatchMapping("/posts/{postId}/comments/{commentId}/status")
	public BaseResponse<Long> statusToDeletePostComment(@PathVariable Long postId, @PathVariable Long commentId) {
		return new BaseResponse<>(commentService.statusToDeletePostComment(postId, commentId));
	}

}
