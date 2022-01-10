package com.sikhye.chabak.service.place.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;

public interface PlaceCommentRepository extends JpaRepository<PlaceComment, Long> {

	Optional<List<PlaceComment>> findPlaceCommentsByPlaceIdAndStatus(Long placeId, BaseStatus status);

	PlaceComment findPlaceCommentByIdAndStatus(Long commentId, BaseStatus status);

	// count + where 쿼리
	Long countByPlaceIdAndStatus(Long placeId, BaseStatus status);
}
