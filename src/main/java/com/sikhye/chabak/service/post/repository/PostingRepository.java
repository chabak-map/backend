package com.sikhye.chabak.service.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.post.entity.Posting;

public interface PostingRepository extends JpaRepository<Posting, Long>, PostingRepositoryCustom {

	Optional<List<Posting>> findPostingsByStatus(BaseStatus status);

	Optional<List<Posting>> findPostingsByMemberIdAndStatus(Long memberId, BaseStatus status);

	Optional<Posting> findPostingByIdAndStatus(Long postingId, BaseStatus status);
}

