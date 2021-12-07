package com.sikhye.chabak.service.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.comment.entity.PlaceReview;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {

	Optional<List<PlaceReview>> findPlaceReviewsByPlaceIdAndStatus(Long placeId, BaseStatus status);

	PlaceReview findPlaceReviewByIdAndStatus(Long reviewId, BaseStatus status);

	// count + where 쿼리
	Long countByPlaceIdAndStatus(Long placeId, BaseStatus status);
}
