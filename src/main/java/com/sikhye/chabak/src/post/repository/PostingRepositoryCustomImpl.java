package com.sikhye.chabak.src.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Slf4j
@Repository
public class PostingRepositoryCustomImpl implements PostingRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public PostingRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
}
