package com.sikhye.chabak.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.service.post.PostingService;
import com.sikhye.chabak.service.post.dto.PostingDetailRes;
import com.sikhye.chabak.service.post.dto.PostingReq;
import com.sikhye.chabak.service.post.dto.PostingRes;

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

	// >> ptpt: 이미지까지 한 번에 넣어주면 글 + 이미지 동시 입력 가능
	@PostMapping
	public BaseResponse<Long> addPosts(@Valid PostingReq postingReq) {
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

}
