package com.sikhye.chabak.src.post;

import com.sikhye.chabak.base.BaseResponse;
import com.sikhye.chabak.src.post.dto.PostingDetailRes;
import com.sikhye.chabak.src.post.dto.PostingReq;
import com.sikhye.chabak.src.post.dto.PostingRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostingController {

	private final PostingService postingService;

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
