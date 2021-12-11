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
import com.sikhye.chabak.service.post.PostingService;
import com.sikhye.chabak.service.post.dto.PostingCommentReq;
import com.sikhye.chabak.service.post.dto.PostingCommentRes;
import com.sikhye.chabak.service.post.dto.PostingDetailRes;
import com.sikhye.chabak.service.post.dto.PostingReq;
import com.sikhye.chabak.service.post.dto.PostingRes;
import com.sikhye.chabak.service.post.dto.PostingTagReq;
import com.sikhye.chabak.service.post.dto.PostingTagRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostingController {

	private final PostingService postingService;

	public PostingController(PostingService postingService) {
		this.postingService = postingService;
	}

	@GetMapping
	public BaseResponse<List<PostingRes>> findAllPosts() {
		return new BaseResponse<>(postingService.findPosts());
	}

	@GetMapping("/me")
	public BaseResponse<List<PostingRes>> findMyPosts() {
		return new BaseResponse<>(postingService.findMemberPosts());
	}

	// >> ptpt: 이미지까지 한 번에 넣어주면 글 + 이미지 동시 입력 가능 >> 생성자가 아니라 setter 이용해야 함
	@PostMapping
	public BaseResponse<Long> addPosts(@Valid @RequestBody PostingReq postingReq) {

		return new BaseResponse<>(postingService.createPost(postingReq));
	}

	@GetMapping("/{postId}/detail")
	public BaseResponse<PostingDetailRes> findPostDetail(@PathVariable Long postId) {
		return new BaseResponse<>(postingService.findPostDetail(postId));
	}

	// TODO: 포스팅 수정 ( 글 수정만? )
	@PatchMapping("/{postId}")
	public BaseResponse<Long> editPost(@RequestBody PostingReq postingReq, @PathVariable Long postId) {
		return new BaseResponse<>(postingService.editPost(postingReq, postId));
	}

	@PatchMapping("/{postId}/status")
	public BaseResponse<Long> statusDeleteToPost(@PathVariable Long postId) {
		return new BaseResponse<>(postingService.statusToDeletePost(postId));
	}

	@GetMapping("/{postId}/tags")
	public BaseResponse<List<String>> getPostingTags(@PathVariable Long postId) {
		return new BaseResponse<>(postingService.findPostingTags(postId));
	}

	@PostMapping("/{postId}/tags")
	public BaseResponse<List<PostingTagRes>> addPostingTags(@PathVariable Long postId,
		@RequestBody PostingTagReq postingTagReq) {
		return new BaseResponse<>(postingService.addPostingTags(postId, postingTagReq));
	}

	@PatchMapping("/{postId}/tags/{tagId}")
	public BaseResponse<Long> editPostingTag(@PathVariable Long postId,
		@PathVariable Long tagId,
		@RequestParam String tagName) {
		return new BaseResponse<>(postingService.editPostingTag(postId, tagId, tagName));
	}

	@PatchMapping("/{postId}/tags/{tagId}/status")
	public BaseResponse<Long> statusToDeletePostingTag(@PathVariable Long postId,
		@PathVariable Long tagId) {
		return new BaseResponse<>(postingService.postingTagStatusToDelete(postId, tagId));
	}

	@GetMapping("/{postId}/comments")
	public BaseResponse<List<PostingCommentRes>> findPostComments(@PathVariable Long postId) {
		return new BaseResponse<>(postingService.findPostComments(postId));
	}

	@PostMapping("/{postId}/comments")
	public BaseResponse<Long> addPostComment(@PathVariable Long postId,
		@Valid @RequestBody PostingCommentReq commentReq) {
		return new BaseResponse<>(postingService.addPostComment(postId, commentReq));
	}

	@PatchMapping("/{postId}/comments/{commentId}")
	public BaseResponse<Long> editPostComment(@PathVariable Long postId, @PathVariable Long commentId,
		@Valid @RequestBody PostingCommentReq commentReq) {
		return new BaseResponse<>(postingService.editPostComment(postId, commentId, commentReq));
	}

	@PatchMapping("/{postId}/comments/{commentId}/status")
	public BaseResponse<Long> statusToDeletePostComment(@PathVariable Long postId, @PathVariable Long commentId) {
		return new BaseResponse<>(postingService.statusToDeletePostComment(postId, commentId));
	}
}
