// package com.sikhye.chabak.service.search.domain;
//
// import java.util.List;
// import java.util.Optional;
//
// import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//
// import com.sikhye.chabak.global.constant.BaseStatus;
// import com.sikhye.chabak.service.post.domain.Posting;
//
// public interface PostSearchRepository extends ElasticsearchRepository<Posting, Long> {
//
// 	Optional<List<Posting>> findByTitleContainsOrContentContainsAndStatus(String title, String content,
// 		BaseStatus status);
//
// 	Optional<Posting> findByIdAndStatus(Long postId, BaseStatus status);
//
// }
