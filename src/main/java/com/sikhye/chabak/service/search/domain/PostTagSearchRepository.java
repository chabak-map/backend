// package com.sikhye.chabak.service.search.domain;
//
// import java.util.List;
// import java.util.Optional;
//
// import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//
// import com.sikhye.chabak.global.constant.BaseStatus;
// import com.sikhye.chabak.service.post.entity.PostingTag;
//
// public interface PostTagSearchRepository extends ElasticsearchRepository<PostingTag, Long> {
//
// 	Optional<List<PostingTag>> findByNameAndStatus(String name, BaseStatus status);
//
// }
