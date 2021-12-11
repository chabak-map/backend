package com.sikhye.chabak.service.place.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.place.entity.PlaceComment;

public interface PlaceCommentRepository extends JpaRepository<PlaceComment, Long> {

	Optional<List<PlaceComment>> findPlaceCommentsByPlaceIdAndStatus(Long placeId, BaseStatus status);

	PlaceComment findPlaceCommentByIdAndStatus(Long commentId, BaseStatus status);

	// count + where 쿼리
	Long countByPlaceIdAndStatus(Long placeId, BaseStatus status);
}
