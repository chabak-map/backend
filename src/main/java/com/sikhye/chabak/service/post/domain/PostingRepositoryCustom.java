package com.sikhye.chabak.service.post.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostingRepositoryCustom {

	Page<Posting> findPageByStatusQueryDSL1(Pageable pageable);
}

