package com.sikhye.chabak.src.post.repository;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.post.entity.PostingComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingCommentRepository extends JpaRepository<PostingComment, Long> {

	// count + where 쿼리
	Long countByPostingIdAndStatus(Long postingId, BaseStatus status);
}
