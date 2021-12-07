package com.sikhye.chabak.service.place.repository;

import com.sikhye.chabak.service.place.dto.PlaceSearchRes;

import java.util.List;
import java.util.Optional;

public interface PlaceRepositoryCustom {

	Optional<List<PlaceSearchRes>> findPlaceNearbyPoint(Double lat, Double lng, Double radius);
}
