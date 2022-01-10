package com.sikhye.chabak.service.place.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, String> {

	Optional<District> findByRegion1DepthContainingAndRegion2DepthContaining(String region1Depth, String region2Depth);
}
