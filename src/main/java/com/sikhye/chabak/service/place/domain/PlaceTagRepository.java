package com.sikhye.chabak.service.place.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;

public interface PlaceTagRepository extends JpaRepository<PlaceTag, Long> {

	Optional<List<PlaceTag>> findPlaceTagsByPlaceIdAndStatus(Long placeId, BaseStatus status);

	Optional<PlaceTag> findPlaceTagByIdAndStatus(Long placeTagId, BaseStatus status);

	Optional<List<PlaceTag>> findByNameAndStatus(String tagName, BaseStatus status);
}
