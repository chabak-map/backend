package com.sikhye.chabak.service.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.comment.entity.PostingComment;

public interface PostingCommentRepository extends JpaRepository<PostingComment, Long> {

	Optional<List<PostingComment>> findPostingCommentsByPostingIdAndStatus(Long postId, BaseStatus status);

	PostingComment findPostingCommentByIdAndStatus(Long commentId, BaseStatus status);

	// count + where 쿼리
	Long countByPostingIdAndStatus(Long postingId, BaseStatus status);
}
