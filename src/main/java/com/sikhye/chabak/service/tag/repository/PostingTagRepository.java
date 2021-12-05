package com.sikhye.chabak.service.tag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.time.BaseStatus;
import com.sikhye.chabak.service.tag.entity.PostingTag;

public interface PostingTagRepository extends JpaRepository<PostingTag, Long> {

	Optional<List<PostingTag>> findPostingTagsByPostingIdAndStatus(Long postingId, BaseStatus status);

	Optional<PostingTag> findPostingTagByIdAndStatus(Long postingId, BaseStatus status);
}
