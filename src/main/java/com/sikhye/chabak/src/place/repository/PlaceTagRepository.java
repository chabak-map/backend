package com.sikhye.chabak.src.place.repository;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.place.entity.PlaceTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceTagRepository extends JpaRepository<PlaceTag, Long> {

	Optional<List<PlaceTag>> findTagListByPlaceIdAndStatus(Long placeId, BaseStatus status);
}
