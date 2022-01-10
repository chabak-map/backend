package com.sikhye.chabak.service.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sikhye.chabak.service.place.PlaceService;
import com.sikhye.chabak.service.place.domain.Place;
import com.sikhye.chabak.service.place.domain.PlaceImage;
import com.sikhye.chabak.service.place.domain.PlaceTag;
import com.sikhye.chabak.service.post.PostingService;
import com.sikhye.chabak.service.post.domain.Posting;
import com.sikhye.chabak.service.post.domain.PostingImage;
import com.sikhye.chabak.service.post.domain.PostingTag;
import com.sikhye.chabak.service.search.dto.SearchDto;
import com.sikhye.chabak.service.search.dto.SearchPlaceRes;
import com.sikhye.chabak.service.search.dto.SearchPostRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SearchServiceRdbImpl implements SearchService {

	private final PostingService postingService;
	private final PlaceService placeService;

	public SearchServiceRdbImpl(PostingService postingService, PlaceService placeService) {
		this.postingService = postingService;
		this.placeService = placeService;
	}

	@Override
	public SearchDto searchBy(String keyword) {
		List<Place> findPlaces = placeService.searchPlacesBy(keyword);

		List<Posting> findPosts = postingService
			.searchPostsBy(keyword);

		return convertToSearchDTO(findPlaces, findPosts);
	}

	@Override
	public SearchDto searchByTag(String tagName) {
		List<PlaceTag> findPlaceTags = placeService.searchPlaceTagsBy(tagName);

		List<PostingTag> findPostTags = postingService.searchPostTagsBy(tagName);

		List<Place> findPlaces = findPlaceTags.stream()
			.map(tag -> placeService.findBy(tag.getPlaceId()).orElse(null))
			.collect(Collectors.toList());

		List<Posting> findPosts = findPostTags.stream()
			.map(tag -> postingService.findBy(tag.getPostingId()).orElse(null))
			.collect(Collectors.toList());

		return convertToSearchDTO(findPlaces, findPosts);

	}

	private SearchDto convertToSearchDTO(List<Place> places, List<Posting> posts) {
		List<SearchPlaceRes> searchPlaces = places.stream()
			.map(place -> SearchPlaceRes.builder()
				.id(place.getId())
				.address(place.getAddress())
				.name(place.getName())
				.imageUrl(place.getPlaceImages().stream()
					.limit(1)
					.map(PlaceImage::getImageUrl)
					.collect(Collectors.joining()))
				.build())
			.collect(Collectors.toList());

		List<SearchPostRes> searchPosts = posts.stream()
			.map(post -> SearchPostRes.builder()
				.id(post.getId())
				.title(post.getTitle())
				// .content(post.getContent().substring(0, 29))
				.content(post.getContent())
				.imageUrl(post.getPostingImages().stream()
					.limit(1)
					.map(PostingImage::getImageUrl)
					.collect(Collectors.joining()))
				.build())
			.collect(Collectors.toList());

		return SearchDto.builder()
			.places(searchPlaces)
			.posts(searchPosts)
			.build();
	}
}
