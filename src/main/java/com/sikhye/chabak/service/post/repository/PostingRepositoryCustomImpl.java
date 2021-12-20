package com.sikhye.chabak.service.post.repository;

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
import com.sikhye.chabak.service.post.entity.Posting;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PostingRepositoryCustomImpl implements PostingRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public PostingRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	// 01. 전체 카운트를 한 번에 조회하는 단순한 방법
	@Override
	public Page<Posting> findPageByStatusQueryDSL1(Pageable pageable) {
		List<Posting> results = queryFactory
			.selectFrom(posting)
			.where(posting.status.eq(USED))
			.orderBy(posting.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			// .fetchResults(); // 01. 페이징 정보를 함께 리턴하는 메소드로써, 쿼리가 2번 나간다.
			.fetch();    // 02. count 따로 가져오려는 경우, 쿼리결과는 fetch를 쓴다.

		// List<Posting> postings = results.getResults();    // 전체결과물
		// long total = results.getTotal();        // 01. countQuery

		// 002. 카운트쿼리를 직접 작성도 가능하다.
		// long total = queryFactory
		// 	.select(posting)
		// 	.from(posting)
		// 	.where(posting.status.eq(USED))
		// 	.fetchCount();

		// 01. 일반적 사용
		// return new PageImpl<>(postings, pageable, total);

		// 002-1. 카운트쿼리를 직접 작성도 가능하다. 여기서는 fetch를 사용하지 않았다.
		JPAQuery<Posting> total = queryFactory
			.select(posting)
			.from(posting)
			.where(posting.status.eq(USED));

		// 02. 카운트 쿼리를 직접 작성한 경우
		/***
		 * PageableExecutionUtils.getPage는 PageImpl과 같은 역할을 하지만 한가지 기가막힌 점은 마지막 인자로 함수를 전달하는데
		 * 내부 작동에 의해서 토탈카운트가 페이지사이즈 보다 적거나 ,
		 * 마지막페이지 일 경우 해당 함수를 실행하지 않는다 쿼리를 조금더 줄일 수 있다 .
		 * 위의 코드의 경우 카운트 쿼리 마지막에 fetchCount()가 여기서 관리되고 있다.
		 * PageableExecutionUtils.getPage을 사용하면 조금 더 성능 최적화가 된다.
		 * 쿼리하나가 아쉬울 때는 PageImpl 보다는 PageableExecutionUtils.getPage를 사용하자
		 */
		return PageableExecutionUtils.getPage(results, pageable, total::fetchCount);
	}

}

