package com.sikhye.chabak.service.place.domain;

import java.util.List;
import java.util.Optional;

import com.sikhye.chabak.service.place.dto.PlaceAroundRes;

public interface PlaceRepositoryCustom {

	Optional<List<PlaceAroundRes>> findPlaceNearbyPoint(Double lat, Double lng, Double radius);
}
