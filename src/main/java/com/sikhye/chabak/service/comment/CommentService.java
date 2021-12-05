package com.sikhye.chabak.service.comment;

import com.sikhye.chabak.service.comment.dto.CommentReq;
import com.sikhye.chabak.service.comment.dto.CommentRes;

import java.util.List;

public interface CommentService {

	// 01. 장소 댓글 리스트 조회
	List<CommentRes> findPlaceComments(Long placeId);

	// 02. 장소 댓글 작성
	Long addPlaceComment(Long placeId, CommentReq commentReq);

	// 03. 장소 댓글 수정
	Long editPlaceComment(Long placeId, Long commentId, CommentReq commentReq);

	// 04. 장소 댓글 삭제
	Long statusToDeletePlaceComment(Long placeId, Long commentId);

	// 05. 포스트 댓글 조회
	List<CommentRes> findPostComments(Long postId);

	// 06. 포스트 댓글 작성
	Long addPostComment(Long postId, CommentReq commentReq);

	// 07. 포스트 댓글 수정
	Long editPostComment(Long postId, Long commentId, CommentReq commentReq);

	// 08. 포스트 댓글 삭제
	Long statusToDeletePostComment(Long postId, Long commentId);

}
