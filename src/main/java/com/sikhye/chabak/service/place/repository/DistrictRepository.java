package com.sikhye.chabak.service.place.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.service.place.entity.District;

public interface DistrictRepository extends JpaRepository<District, String> {

	Optional<District> findByRegion1DepthContainingAndRegion2DepthContaining(String region1Depth, String region2Depth);
}
