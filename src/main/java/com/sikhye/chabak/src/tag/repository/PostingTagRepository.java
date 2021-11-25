package com.sikhye.chabak.src.tag.repository;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.tag.entity.PostingTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostingTagRepository extends JpaRepository<PostingTag, Long> {

	Optional<List<PostingTag>> findPostingTagsByPostingIdAndStatus(Long postingId, BaseStatus status);

	Optional<PostingTag> findPostingTagByIdAndStatus(Long postingId, BaseStatus status);
}
