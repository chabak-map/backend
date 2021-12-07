package com.sikhye.chabak.service.post;

import com.sikhye.chabak.service.post.dto.PostingDetailRes;
import com.sikhye.chabak.service.post.dto.PostingReq;
import com.sikhye.chabak.service.post.dto.PostingRes;

import java.util.List;

public interface PostingService {

	// 01. 포스트 전체 조회 API (페이징 이용)
	List<PostingRes> findPostsWithPaging();

	// 01-1. 포스트 전체 조회 API (페이징 이용 X)
	List<PostingRes> findPosts();

	// 02. 본인 작성한 포스팅 조회 API (페이징 이용, jwt)
	List<PostingRes> findMemberPosts();

	// 03. 포스트 등록 API (Jwt)
	Long createPost(PostingReq postingReq);

	// 04. 포스트 상세 조회 API
	PostingDetailRes findPostDetail(Long postId);

	// 05. 포스트 수정 API
	Long editPost(PostingReq postingReq, Long postId);

	// 06. 포스트 삭제 API (jwt 이용)
	Long statusToDeletePost(Long postId);

}
