package com.sikhye.chabak.src.place.repository;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.place.entity.PlaceImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceImageRepository extends JpaRepository<PlaceImage, Long> {

	Optional<List<PlaceImage>> findPlaceImagesByPlaceIdAndStatus(Long placeId, BaseStatus status);

	// 첫 번째 장소만 찾아옴
	Optional<PlaceImage> findFirst1PlaceImageByPlaceIdAndStatus(Long placeId, BaseStatus status);
}
