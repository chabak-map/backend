package com.sikhye.chabak.service.search.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.place.entity.PlaceTag;

public interface PlaceTagSearchRepository extends ElasticsearchRepository<PlaceTag, Long> {

	Optional<List<PlaceTag>> findByNameAndStatus(String name, BaseStatus status);
}
