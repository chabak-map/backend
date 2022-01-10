package com.sikhye.chabak.service.post.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sikhye.chabak.global.constant.BaseStatus;

public interface PostingRepository extends JpaRepository<Posting, Long>, PostingRepositoryCustom {

	Page<Posting> findPageByStatus(BaseStatus status, Pageable pageable);

	@Query(value = "select p from Posting p",
		countQuery = "select count(p.name) from Place p")
	Page<Posting> findPagesWithQueryByStatus(BaseStatus status, Pageable pageable);

	Slice<Posting> findSliceByStatus(BaseStatus status, Pageable pageable);

	Optional<List<Posting>> findPostingsByStatus(BaseStatus status);

	Optional<List<Posting>> findPostingsByMemberIdAndStatus(Long memberId, BaseStatus status);

	Optional<Posting> findPostingByIdAndStatus(Long postingId, BaseStatus status);

	Optional<List<Posting>> findTop4ByStatusOrderByCreatedAtDesc(BaseStatus status);

	// //20211216
	// Optional<List<Posting>> findPostingAllByUpdatedAtAfterAndStatus(LocalDateTime updatedAt, BaseStatus status);
	//
	// int countAllByStatus(BaseStatus status);

	Optional<List<Posting>> findByTitleContainsOrContentContainsAndStatus(String title, String content,
		BaseStatus status);
}

