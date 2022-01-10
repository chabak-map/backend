package com.sikhye.chabak.service.place.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;

public interface PlaceImageRepository extends JpaRepository<PlaceImage, Long> {

	Optional<List<PlaceImage>> findPlaceImagesByPlaceIdAndStatus(Long placeId, BaseStatus status);

}
