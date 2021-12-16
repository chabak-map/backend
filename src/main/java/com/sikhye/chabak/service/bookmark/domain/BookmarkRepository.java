package com.sikhye.chabak.service.bookmark.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	// Query Creation - find
	// select * from Bookmark where member_id = :memberId and status = :status
	// 멤버 ID와 상태가 used인 북마크'들'을 갖고 온다.
	Optional<List<Bookmark>> findBookmarksByMemberIdAndStatus(Long memberId, BaseStatus status);

	// select * from Bookmark where id = :id and status = :status
	Optional<Bookmark> findBookmarkByIdAndStatus(Long id, BaseStatus status);

	Boolean existsBookmarkByMemberIdAndPlaceIdAndStatus(Long memberId, Long placeId, BaseStatus status);
}