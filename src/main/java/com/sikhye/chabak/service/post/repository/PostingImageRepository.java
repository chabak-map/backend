package com.sikhye.chabak.service.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.post.entity.PostingImage;

public interface PostingImageRepository extends JpaRepository<PostingImage, Long> {

	Optional<PostingImage> findTop1ByPostingIdAndStatus(Long postingId, BaseStatus status);
}
