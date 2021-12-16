package com.sikhye.chabak.service.place.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.place.entity.Place;

//20211216
// public interface PlaceRepository extends ElasticsearchRepository<Place, Long>, PlaceRepositoryCustom {
public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom {

	Optional<Place> findPlaceByIdAndStatus(Long placeId, BaseStatus status);

	// ptpt >> LIKE를 사용하면 안 된다, LIKE는 문법을 따른다 ( %키워드% ) 대신에 Containing
	// searchPlaces
	Optional<List<Place>> findPlacesByNameContainingOrAddressContainingAndStatus(String name, String address,
		BaseStatus status);

}
