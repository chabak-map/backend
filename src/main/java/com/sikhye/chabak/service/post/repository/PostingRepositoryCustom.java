package com.sikhye.chabak.service.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sikhye.chabak.service.post.entity.Posting;

public interface PostingRepositoryCustom {

	Page<Posting> findPageByStatusQueryDSL1(Pageable pageable);
}

