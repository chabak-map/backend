package com.sikhye.chabak.src.tag;

import com.sikhye.chabak.base.exception.BaseException;
import com.sikhye.chabak.src.tag.dto.PlaceTagReq;
import com.sikhye.chabak.src.tag.dto.PlaceTagRes;
import com.sikhye.chabak.src.tag.dto.PostingTagReq;
import com.sikhye.chabak.src.tag.dto.PostingTagRes;
import com.sikhye.chabak.src.tag.entity.PlaceTag;
import com.sikhye.chabak.src.tag.entity.PostingTag;
import com.sikhye.chabak.src.tag.repository.PlaceTagRepository;
import com.sikhye.chabak.src.tag.repository.PostingTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.sikhye.chabak.base.BaseResponseStatus.SEARCH_NOT_FOUND_PLACE;
import static com.sikhye.chabak.base.BaseResponseStatus.SEARCH_NOT_FOUND_POST;
import static com.sikhye.chabak.base.entity.BaseStatus.used;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

	private final PlaceTagRepository placeTagRepository;
	private final PostingTagRepository postingTagRepository;


	@Override
	public List<String> findPlaceTags(Long placeId) {
		List<PlaceTag> placeTags = placeTagRepository.findPlaceTagsByPlaceIdAndStatus(placeId, used).orElse(Collections.emptyList());

		return placeTags.stream()
			.map(PlaceTag::getName)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<PlaceTagRes> addPlaceTags(Long placeId, PlaceTagReq placeTagReq) {
		List<String> placeTagNames = placeTagReq.getPlaceTags();

		return placeTagNames.stream()
			.map(s -> {
				PlaceTag toSavePlaceTag = PlaceTag.builder()
					.name(s)
					.placeId(placeId)
					.build();

				PlaceTag savedPlaceTag = placeTagRepository.save(toSavePlaceTag);
				return new PlaceTagRes(savedPlaceTag.getId(), savedPlaceTag.getName());
			})
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Long editPlaceTag(Long placeId, Long placeTagId, String placeTagName) {
		PlaceTag findPlaceTag = placeTagRepository.findPlaceTagByIdAndStatus(placeTagId, used).orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		if (!findPlaceTag.getPlaceId().equals(placeId)) throw new BaseException(SEARCH_NOT_FOUND_PLACE);

		findPlaceTag.setName(placeTagName);

		return placeTagId;
	}

	@Override
	@Transactional
	public Long placeTagStatusToDelete(Long placeId, Long placeTagId) {
		PlaceTag findPlaceTag = placeTagRepository.findPlaceTagByIdAndStatus(placeTagId, used).orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		if (!findPlaceTag.getPlaceId().equals(placeId)) throw new BaseException(SEARCH_NOT_FOUND_PLACE);

		findPlaceTag.setStatusToDelete();

		return placeTagId;
	}

	@Override
	public List<String> findPostingTags(Long postingId) {
		List<PostingTag> postingTags = postingTagRepository.findPostingTagsByPostingIdAndStatus(postingId, used).orElse(Collections.emptyList());

		return postingTags.stream()
			.map(PostingTag::getName)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<PostingTagRes> addPostingTags(Long postingId, PostingTagReq postingTagReq) {
		List<String> postingTagNames = postingTagReq.getPostingTags();

		return postingTagNames.stream()
			.map(s -> {
				PostingTag toSavePostingTag = PostingTag.builder()
					.name(s)
					.postingId(postingId)
					.build();

				PostingTag savedPostingTag = postingTagRepository.save(toSavePostingTag);
				return new PostingTagRes(savedPostingTag.getId(), savedPostingTag.getName());
			})
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Long editPostingTag(Long postingId, Long postingTagId, String postingTagName) {
		PostingTag findPostingTag = postingTagRepository.findPostingTagByIdAndStatus(postingTagId, used).orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_POST));

		if (!findPostingTag.getPostingId().equals(postingId)) throw new BaseException(SEARCH_NOT_FOUND_POST);

		findPostingTag.setName(postingTagName);


		return postingTagId;
	}

	@Override
	@Transactional
	public Long postingTagStatusToDelete(Long postingId, Long postingTagId) {
		PostingTag findPostingTag = postingTagRepository.findPostingTagByIdAndStatus(postingTagId, used).orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_POST));

		if (!findPostingTag.getPostingId().equals(postingId)) throw new BaseException(SEARCH_NOT_FOUND_POST);

		findPostingTag.setStatusToDelete();

		return postingTagId;
	}
}
