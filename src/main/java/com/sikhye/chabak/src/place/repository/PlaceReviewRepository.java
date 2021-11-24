package com.sikhye.chabak.src.place.repository;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.place.entity.PlaceReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {

	Optional<List<PlaceReview>> findPlaceReviewsByPlaceIdAndStatus(Long placeId, BaseStatus status);

	// count + where 쿼리
	Long countByPlaceIdAndStatus(Long placeId, BaseStatus status);
}
