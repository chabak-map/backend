package com.sikhye.chabak.service.bookmark.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	Optional<List<Bookmark>> findBookmarksByMemberIdAndStatus(Long memberId, BaseStatus status);

	Optional<Bookmark> findBookmarkByIdAndStatus(Long id, BaseStatus status);
}
