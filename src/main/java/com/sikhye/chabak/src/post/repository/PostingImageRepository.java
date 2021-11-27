package com.sikhye.chabak.src.post.repository;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.post.entity.PostingImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostingImageRepository extends JpaRepository<PostingImage, Long> {

	Optional<PostingImage> findTop1ByPostingIdAndStatus(Long postingId, BaseStatus status);
}
