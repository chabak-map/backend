package com.sikhye.chabak.service.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.sikhye.chabak.service.post.domain.Posting;
import com.sikhye.chabak.service.post.domain.PostingTag;
import com.sikhye.chabak.service.post.dto.PostingCommentReq;
import com.sikhye.chabak.service.post.dto.PostingCommentRes;
import com.sikhye.chabak.service.post.dto.PostingDetailRes;
import com.sikhye.chabak.service.post.dto.PostingEditReq;
import com.sikhye.chabak.service.post.dto.PostingRecentRes;
import com.sikhye.chabak.service.post.dto.PostingReq;
import com.sikhye.chabak.service.post.dto.PostingRes;
import com.sikhye.chabak.service.post.dto.PostingTagReq;
import com.sikhye.chabak.service.post.dto.PostingTagRes;

public interface PostingService {

	// 01. 포스트 전체 조회 API (페이징 이용)
	List<PostingRes> findPostsWithJpaPaging(int offset, int limit);

	// 01-0. 포스트 전체 조회 API (슬라이싱 이용)
	List<PostingRes> findPostsWithJpaSlicing(int offset, int limit);

	// 01-1. querydsl를 이용한 포스트 전체 조회 API
	Page<PostingRes> findPostsWithQuerydslPaging(Pageable pageable);

	// 01-2. 포스트 전체 조회 API (페이징 이용 X)
	List<PostingRes> findPosts();

	// 02. 본인 작성한 포스팅 조회 API (페이징 이용, jwt)
	List<PostingRes> findMemberPosts();

	// 03. 포스트 등록 API (Jwt)
	Long createPost(PostingReq postingReq);

	// 04. 포스트 상세 조회 API
	PostingDetailRes findPostDetail(Long postId);

	// 05. 포스트 수정 API
	Long editPost(Long postId, PostingEditReq postingEditReq);

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

	// s3 이미지 업로드
	String uploadImage(MultipartFile image, Long postId);

	// s3 이미지 삭제
	Boolean deleteImage(String url, Long postId);

	// 20211216
	//
	// // (ES 전용) 이미지 및 포스팅 전체 조회
	// List<Posting> findPostings();
	//
	// int countPosts();
	//
	// List<PostingImage> findPostingImages();

	// 검색 전용
	List<Posting> searchPostsBy(String keyword);

	List<PostingTag> searchPostTagsBy(String tagName);

	Optional<Posting> findBy(Long id);
}
