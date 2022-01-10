package com.sikhye.chabak.service.post.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;

public interface PostingImageRepository extends JpaRepository<PostingImage, Long> {

	Optional<PostingImage> findTop1ByPostingIdAndStatus(Long postingId, BaseStatus status);

	Optional<List<PostingImage>> findPostingImageAllByStatus(BaseStatus status);
}
