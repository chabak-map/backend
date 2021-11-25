package com.sikhye.chabak.src.tag.repository;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.tag.entity.PlaceTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceTagRepository extends JpaRepository<PlaceTag, Long> {

	Optional<List<PlaceTag>> findPlaceTagsByPlaceIdAndStatus(Long placeId, BaseStatus status);

	Optional<PlaceTag> findPlaceTagByIdAndStatus(Long placeTagId, BaseStatus status);


}
