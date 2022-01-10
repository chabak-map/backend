package com.sikhye.chabak.service.place.domain;

import static com.querydsl.core.types.dsl.Expressions.*;
import static com.querydsl.core.types.dsl.MathExpressions.*;
import static com.sikhye.chabak.service.place.domain.QPlace.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.place.dto.PlaceAroundRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	//20211216
	// private final ElasticsearchOperations elasticsearchOperations;    // Document, Search 기능을 모두 가지고 있음

	//20211216
	// public PlaceRepositoryCustomImpl(EntityManager em,
	// 	ElasticsearchOperations elasticsearchOperations) {
	// 	this.queryFactory = new JPAQueryFactory(em);
	// 	this.elasticsearchOperations = elasticsearchOperations;
	// }

	public PlaceRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Optional<List<PlaceAroundRes>> findPlaceNearbyPoint(Double lat, Double lng, Double radius) {

		// Haversine 공식
		NumberPath<Double> distance = Expressions.numberPath(Double.class, "distance");

		Optional<List<PlaceAroundRes>> resultLists = Optional.ofNullable(queryFactory
			.select(Projections.constructor(PlaceAroundRes.class,
				place.id,
				round(acos(
					cos(radians(place.longitude).subtract(radians(asNumber(lng))))
						.multiply(cos(radians(asNumber(lat))))
						.multiply(cos(radians(place.latitude)))
						.add(sin(radians(asNumber(lat))).multiply(sin(radians(place.latitude)))))
					.multiply(asNumber(6371)), 3)
					.as(String.valueOf(distance)),
				place.latitude,
				place.longitude))
			.from(place)
			.where(place.status.eq(BaseStatus.USED)
				.and(
					acos(
						cos(radians(place.longitude).subtract(radians(asNumber(lng))))
							.multiply(cos(radians(asNumber(lat))))
							.multiply(cos(radians(place.latitude)))
							.add(sin(radians(asNumber(lat))).multiply(sin(radians(place.latitude)))))
						.multiply(asNumber(6371)).loe(radius)))
			.orderBy(distance.asc())
			.fetch());

		return resultLists;
	}

}
