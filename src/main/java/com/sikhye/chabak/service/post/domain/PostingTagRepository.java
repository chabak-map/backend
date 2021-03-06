package com.sikhye.chabak.service.post.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;

public interface PostingTagRepository extends JpaRepository<PostingTag, Long> {

	Optional<List<PostingTag>> findPostingTagsByPostingIdAndStatus(Long postingId, BaseStatus status);

	Optional<PostingTag> findPostingTagByIdAndStatus(Long postingId, BaseStatus status);

	Optional<List<PostingTag>> findByNameAndStatus(String name, BaseStatus status);
}
