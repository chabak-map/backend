// package com.sikhye.chabak.service.search.domain;
//
// import java.util.List;
// import java.util.Optional;
//
// import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//
// import com.sikhye.chabak.global.constant.BaseStatus;
// import com.sikhye.chabak.service.place.domain.Place;
//
// public interface PlaceSearchRepository extends ElasticsearchRepository<Place, Long> {
//
// 	Optional<List<Place>> findByNameContainsAndStatus(String name, BaseStatus status);
//
// 	Optional<Place> findByIdAndStatus(Long placeId, BaseStatus status);
//
// }
