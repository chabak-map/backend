package com.sikhye.chabak.src.post.repository;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.post.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostingRepository extends JpaRepository<Posting, Long>, PostingRepositoryCustom {

	Optional<List<Posting>> findPostingsByStatus(BaseStatus status);

	Optional<List<Posting>> findPostingsByMemberIdAndStatus(Long memberId, BaseStatus status);

	Optional<Posting> findPostingByIdAndStatus(Long postingId, BaseStatus status);
}

