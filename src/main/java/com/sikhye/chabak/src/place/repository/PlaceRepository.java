package com.sikhye.chabak.src.place.repository;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom {

	Optional<Place> findPlaceByIdAndStatus(Long placeId, BaseStatus status);

}
