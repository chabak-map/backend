package com.sikhye.chabak.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.service.post.PostingService;
import com.sikhye.chabak.service.post.dto.PostingCommentReq;
import com.sikhye.chabak.service.post.dto.PostingCommentRes;
import com.sikhye.chabak.service.post.dto.PostingDetailRes;
import com.sikhye.chabak.service.post.dto.PostingEditReq;
import com.sikhye.chabak.service.post.dto.PostingRecentRes;
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

	@GetMapping("/page")
	public BaseResponse<List<PostingRes>> findAllJpaPagePosts(
		@RequestParam(defaultValue = "0") Integer offset,
		@RequestParam(defaultValue = "10") Integer limit) {
		return new BaseResponse<>(postingService.findPostsWithJpaPaging(offset, limit));
	}

	@GetMapping("/slice")
	public BaseResponse<List<PostingRes>> findAllJpaSlicePosts(
		@RequestParam(defaultValue = "0") Integer offset,
		@RequestParam(defaultValue = "10") Integer limit) {
		return new BaseResponse<>(postingService.findPostsWithJpaSlicing(offset, limit));
	}

	// http://localhost:9000/posts/querydsl?size=5&page=2
	@GetMapping("/querydsl")
	public BaseResponse<Page<PostingRes>> findAllQuerydslPosts(@PageableDefault Pageable pageable) {
		return new BaseResponse<>(postingService.findPostsWithQuerydslPaging(pageable));
	}

	@GetMapping("/me")
	public BaseResponse<List<PostingRes>> findMyPosts() {
		return new BaseResponse<>(postingService.findMemberPosts());
	}

	@PostMapping
	public BaseResponse<Long> addPosts(@Valid @RequestBody PostingReq postingReq) {

		return new BaseResponse<>(postingService.createPost(postingReq));
	}

	@GetMapping("/{postId}/detail")
	public BaseResponse<PostingDetailRes> findPostDetail(@PathVariable Long postId) {
		return new BaseResponse<>(postingService.findPostDetail(postId));
	}

	@PatchMapping("/{postId}")
	public BaseResponse<Long> editPost(@PathVariable Long postId, @RequestBody PostingEditReq postingEditReq) {
		return new BaseResponse<>(postingService.editPost(postId, postingEditReq));
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

	@GetMapping("/recency")
	public BaseResponse<List<PostingRecentRes>> top4RecentPosts() {
		return new BaseResponse<>(postingService.getTop4RecentPosts());
	}

	@GetMapping("/members/{memberId}")
	public BaseResponse<List<PostingRes>> findMemberPosts(@PathVariable Long memberId) {
		return new BaseResponse<>(postingService.findMemberPosts(memberId));
	}

	@PostMapping("/s3/upload")
	public BaseResponse<String> uploadImage(@RequestPart MultipartFile image, Long postId) {
		return new BaseResponse<>(postingService.uploadImage(image, postId));
	}

	@DeleteMapping("/s3/delete")
	public BaseResponse<Boolean> deleteImage(@RequestParam String url, Long postId) {
		return new BaseResponse<>(postingService.deleteImage(url, postId));
	}
}
