package com.sikhye.chabak.service.post.domain;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.service.post.entity.QPosting.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PostingRepositoryCustomImpl implements PostingRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public PostingRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Posting> findPageByStatusQueryDSL1(Pageable pageable) {
		List<Posting> results = queryFactory
			.selectFrom(posting)
			.where(posting.status.eq(USED))
			.orderBy(posting.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			// .fetchResults(); // 01. 페이징 정보를 함께 리턴하는 메소드로써, 쿼리가 2번 나간다.
			.fetch();

		JPAQuery<Posting> total = queryFactory
			.select(posting)
			.from(posting)
			.where(posting.status.eq(USED));

		return PageableExecutionUtils.getPage(results, pageable, total::fetchCount);
	}

}

