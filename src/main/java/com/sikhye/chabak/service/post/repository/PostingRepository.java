package com.sikhye.chabak.service.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.post.entity.Posting;

public interface PostingRepository extends JpaRepository<Posting, Long>, PostingRepositoryCustom {

	Page<Posting> findPageByStatus(BaseStatus status, Pageable pageable);

	// count 쿼리는 무겁기 때문에 복잡한 쿼리에서는 카운트쿼리는 따로 분리한다.
	@Query(value = "select p from Posting p",
		countQuery = "select count(p.name) from Place p")
	Page<Posting> findPagesWithQueryByStatus(BaseStatus status, Pageable pageable);

	// Slice<T> 타입은 추가 count 쿼리 없이 다음 페이지 확인 가능하다. 내부적으로 limit + 1 조회를 해서 totalCount 쿼리가 나가지 않아서 성능상 조금 이점을 볼 수도 있다.
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

	Optional<Posting> findByIdAndStatus(Long postId, BaseStatus status);
}

