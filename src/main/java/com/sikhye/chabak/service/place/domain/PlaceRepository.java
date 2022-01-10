package com.sikhye.chabak.service.place.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;

//20211216
// public interface PlaceRepository extends ElasticsearchRepository<Place, Long>, PlaceRepositoryCustom {
public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom {

	Optional<Place> findPlaceByIdAndStatus(Long placeId, BaseStatus status);

	Optional<List<Place>> findPlacesByNameContainingOrAddressContainingAndStatus(String name, String address,
		BaseStatus status);

	Optional<List<Place>> findPlacesByDistrictCodeAndStatus(String code, BaseStatus status);

	Optional<List<Place>> findByNameContainsAndStatus(String name, BaseStatus status);

}
