package com.sikhye.chabak.src.place.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.place.dto.PlaceSearchRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.asNumber;
import static com.querydsl.core.types.dsl.MathExpressions.*;
import static com.sikhye.chabak.src.place.entity.QPlace.place;

@Slf4j
@Repository
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public PlaceRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Optional<List<PlaceSearchRes>> findPlaceNearbyPoint(Double lat, Double lng, Double radius) {
		// Haversine 공식
		NumberPath<Double> distance = Expressions.numberPath(Double.class, "distance");
		Optional<List<PlaceSearchRes>> resultLists = Optional.ofNullable(queryFactory
			.select(Projections.constructor(PlaceSearchRes.class,
				place.id,
				acos(
					cos(radians(place.longitude).subtract(radians(asNumber(lng))))
						.multiply(cos(radians(asNumber(lat))))
						.multiply(cos(radians(place.latitude)))
						.add(sin(radians(asNumber(lat))).multiply(sin(radians(place.latitude)))))
					.multiply(asNumber(6371))
					.as(String.valueOf(distance)),
				place.latitude,
				place.longitude))
			.from(place)
			.where(place.status.eq(BaseStatus.used)
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
