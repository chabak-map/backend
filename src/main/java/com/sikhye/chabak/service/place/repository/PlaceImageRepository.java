package com.sikhye.chabak.service.place.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.place.entity.PlaceImage;

public interface PlaceImageRepository extends JpaRepository<PlaceImage, Long> {

	Optional<List<PlaceImage>> findPlaceImagesByPlaceIdAndStatus(Long placeId, BaseStatus status);

	// 첫 번째 장소만 찾아옴
	Optional<PlaceImage> findFirst1PlaceImageByPlaceIdAndStatus(Long placeId, BaseStatus status);
}
