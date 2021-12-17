package com.sikhye.chabak.service.post;

import java.util.List;

import com.sikhye.chabak.service.post.dto.PostingCommentReq;
import com.sikhye.chabak.service.post.dto.PostingCommentRes;
import com.sikhye.chabak.service.post.dto.PostingDetailRes;
import com.sikhye.chabak.service.post.dto.PostingRecentRes;
import com.sikhye.chabak.service.post.dto.PostingReq;
import com.sikhye.chabak.service.post.dto.PostingRes;
import com.sikhye.chabak.service.post.dto.PostingTagReq;
import com.sikhye.chabak.service.post.dto.PostingTagRes;

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

	// 포스팅 태그 조회 API
	List<String> findPostingTags(Long postingId);

	// 포스팅 태그 등록 API
	List<PostingTagRes> addPostingTags(Long placeId, PostingTagReq postingTagReq);

	// 포스팅 태그 수정 API
	Long editPostingTag(Long postingId, Long postingTagId, String postingTagName);

	// 포스팅 태그 삭제 API
	Long postingTagStatusToDelete(Long postingId, Long postingTagId);

	// 05. 포스트 댓글 조회
	List<PostingCommentRes> findPostComments(Long postId);

	// 06. 포스트 댓글 작성
	Long addPostComment(Long postId, PostingCommentReq commentReq);

	// 07. 포스트 댓글 수정
	Long editPostComment(Long postId, Long commentId, PostingCommentReq commentReq);

	// 08. 포스트 댓글 삭제
	Long statusToDeletePostComment(Long postId, Long commentId);

	// 최근 4개 포스트 정보
	List<PostingRecentRes> getTop4RecentPosts();

	// 특정 상대 작성한 포스팅 조회 API
	List<PostingRes> findMemberPosts(Long memberId);
}
