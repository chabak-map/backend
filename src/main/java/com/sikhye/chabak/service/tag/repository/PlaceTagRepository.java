package com.sikhye.chabak.service.tag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.tag.entity.PlaceTag;

public interface PlaceTagRepository extends JpaRepository<PlaceTag, Long> {

	Optional<List<PlaceTag>> findPlaceTagsByPlaceIdAndStatus(Long placeId, BaseStatus status);

	Optional<PlaceTag> findPlaceTagByIdAndStatus(Long placeTagId, BaseStatus status);

}
