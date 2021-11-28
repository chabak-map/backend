package com.sikhye.chabak.src.tag;

import com.sikhye.chabak.src.tag.dto.PlaceTagReq;
import com.sikhye.chabak.src.tag.dto.PlaceTagRes;
import com.sikhye.chabak.src.tag.dto.PostingTagReq;
import com.sikhye.chabak.src.tag.dto.PostingTagRes;

import java.util.List;

public interface TagService {

	// 장소 태그 조회 API
	List<String> findPlaceTags(Long placeId);

	// 장소 태그 등록 API
	List<PlaceTagRes> addPlaceTags(Long placeId, PlaceTagReq placeTagReq);

	// 장소 태그 수정 API
	Long editPlaceTag(Long placeId, Long placeTagId, String placeTagName);

	// 장소 태그 삭제 API
	Long placeTagStatusToDelete(Long placeId, Long placeTagId);


	// 포스팅 태그 조회 API
	List<String> findPostingTags(Long postingId);

	// 포스팅 태그 등록 API
	List<PostingTagRes> addPostingTags(Long placeId, PostingTagReq postingTagReq);

	// 포스팅 태그 수정 API
	Long editPostingTag(Long postingId, Long postingTagId, String postingTagName);

	// 포스팅 태그 삭제 API
	Long postingTagStatusToDelete(Long postingId, Long postingTagId);


}
