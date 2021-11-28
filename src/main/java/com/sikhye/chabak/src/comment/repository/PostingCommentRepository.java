package com.sikhye.chabak.src.comment.repository;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.comment.entity.PostingComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostingCommentRepository extends JpaRepository<PostingComment, Long> {

	Optional<List<PostingComment>> findPostingCommentsByPostingIdAndStatus(Long postId, BaseStatus status);

	PostingComment findPostingCommentByIdAndStatus(Long commentId, BaseStatus status);

	// count + where 쿼리
	Long countByPostingIdAndStatus(Long postingId, BaseStatus status);
}
