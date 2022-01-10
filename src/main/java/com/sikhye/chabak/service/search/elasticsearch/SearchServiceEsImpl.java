// package com.sikhye.chabak.service.search;
//
// import static com.sikhye.chabak.global.constant.BaseStatus.*;
//
// import java.util.Collections;
// import java.util.List;
// import java.util.stream.Collectors;
//
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.sikhye.chabak.service.place.domain.Place;
// import com.sikhye.chabak.service.place.domain.PlaceImage;
// import com.sikhye.chabak.service.place.domain.PlaceTag;
// import com.sikhye.chabak.service.post.domain.Posting;
// import com.sikhye.chabak.service.post.domain.PostingImage;
// import com.sikhye.chabak.service.post.domain.PostingTag;
// import com.sikhye.chabak.service.search.domain.PlaceSearchRepository;
// import com.sikhye.chabak.service.search.domain.PlaceTagSearchRepository;
// import com.sikhye.chabak.service.search.domain.PostSearchRepository;
// import com.sikhye.chabak.service.search.domain.PostTagSearchRepository;
// import com.sikhye.chabak.service.search.dto.SearchDto;
// import com.sikhye.chabak.service.search.dto.SearchPlaceRes;
// import com.sikhye.chabak.service.search.dto.SearchPostRes;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Service
// @Transactional(readOnly = true)
// public class SearchServiceImpl implements SearchService {
//
// 	private final PlaceSearchRepository placeSearchRepository;
// 	private final PostSearchRepository postSearchRepository;
// 	private final PlaceTagSearchRepository placeTagSearchRepository;
// 	private final PostTagSearchRepository postTagSearchRepository;
//
// 	public SearchServiceImpl(PlaceSearchRepository placeSearchRepository,
// 		PostSearchRepository postSearchRepository,
// 		PlaceTagSearchRepository placeTagSearchRepository,
// 		PostTagSearchRepository postTagSearchRepository) {
// 		this.placeSearchRepository = placeSearchRepository;
// 		this.postSearchRepository = postSearchRepository;
// 		this.placeTagSearchRepository = placeTagSearchRepository;
// 		this.postTagSearchRepository = postTagSearchRepository;
// 	}
//
// 	@Override
// 	public SearchDto searchBy(String keyword) {
// 		List<Place> findPlaces = placeSearchRepository.findByNameContainsAndStatus(keyword, USED)
// 			.orElseGet(Collections::emptyList);
//
// 		List<Posting> findPosts = postSearchRepository
// 			.findByTitleContainsOrContentContainsAndStatus(keyword, keyword, USED)
// 			.orElseGet(Collections::emptyList);
//
// 		return convertToSearchDTO(findPlaces, findPosts);
// 	}
//
// 	@Override
// 	public SearchDto searchByTag(String tagName) {
// 		List<PlaceTag> findPlaceTags = placeTagSearchRepository.findByNameAndStatus(tagName, USED)
// 			.orElseGet(Collections::emptyList);
//
// 		List<PostingTag> findPostTags = postTagSearchRepository.findByNameAndStatus(tagName, USED)
// 			.orElseGet(Collections::emptyList);
//
// 		List<Place> findPlaces = findPlaceTags.stream()
// 			.map(tag -> placeSearchRepository.findByIdAndStatus(tag.getPlaceId(), USED).orElse(null))
// 			.collect(Collectors.toList());
//
// 		List<Posting> findPosts = findPostTags.stream()
// 			.map(tag -> postSearchRepository.findByIdAndStatus(tag.getPostingId(), USED).orElse(null))
// 			.collect(Collectors.toList());
//
// 		return convertToSearchDTO(findPlaces, findPosts);
//
// 	}
//
// 	// 20211216
// 	// @Override
// 	// @Transactional
// 	// public String addPlace(Place place) {
// 	// 	Place savedPlace = placeSearchRepository.save(place);
// 	// 	return savedPlace.getName();
// 	// }
// 	//
// 	// // 관련 SOF: https://stackoverflow.com/questions/68673045/springdatarepository-elasticsearch-works-on-save-but-fails-on-update-with-java-l
// 	// @Override
// 	// public String addPost(Posting post) {
// 	// 	Posting savedPosting = postSearchRepository.save(post);
// 	// 	return savedPosting.getTitle();
// 	// }
// 	//
// 	// // public String testPostingSave() {
// 	// // 	Posting posting = Posting.builder()
// 	// // 		.title("제목123")
// 	// // 		.content("asdf")
// 	// // 		.memberId(109L)
// 	// // 		.build();
// 	// //
// 	// // 	Posting savedPosting = postSearchRepository.save(posting);
// 	// // 	return savedPosting.getContent();
// 	// // }
//
// 	private SearchDto convertToSearchDTO(List<Place> places, List<Posting> posts) {
// 		List<SearchPlaceRes> searchPlaces = places.stream()
// 			.map(place -> SearchPlaceRes.builder()
// 				.id(place.getId())
// 				.address(place.getAddress())
// 				.name(place.getName())
// 				.imageUrl(place.getPlaceImages().stream()
// 					.limit(1)
// 					.map(PlaceImage::getImageUrl)
// 					.collect(Collectors.joining()))
// 				.build())
// 			.collect(Collectors.toList());
//
// 		List<SearchPostRes> searchPosts = posts.stream()
// 			.map(post -> SearchPostRes.builder()
// 				.id(post.getId())
// 				.title(post.getTitle())
// 				.content(post.getContent().substring(0, 29))
// 				.imageUrl(post.getPostingImages().stream()
// 					.limit(1)
// 					.map(PostingImage::getImageUrl)
// 					.collect(Collectors.joining()))
// 				.build())
// 			.collect(Collectors.toList());
//
// 		return SearchDto.builder()
// 			.places(searchPlaces)
// 			.posts(searchPosts)
// 			.build();
// 	}
//
// }
