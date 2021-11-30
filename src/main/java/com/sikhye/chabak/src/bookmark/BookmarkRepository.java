package com.sikhye.chabak.src.bookmark;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	// Query Creation - find
	// select * from Bookmark where member_id = :memberId and status = :status
	// 멤버 ID와 상태가 used인 북마크'들'을 갖고 온다.
	Optional<List<Bookmark>> findBookmarksByMemberIdAndStatus(Long memberId, BaseStatus status);

	// select * from Bookmark where id = :id and status = :status
	Optional<Bookmark> findBookmarkByIdAndStatus(Long id, BaseStatus status);
}