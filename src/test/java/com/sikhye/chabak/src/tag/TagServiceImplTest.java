package com.sikhye.chabak.src.tag;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.tag.TagService;
import com.sikhye.chabak.service.tag.dto.PlaceTagReq;
import com.sikhye.chabak.service.tag.dto.PlaceTagRes;
import com.sikhye.chabak.service.tag.dto.PostingTagReq;
import com.sikhye.chabak.service.tag.dto.PostingTagRes;
import com.sikhye.chabak.service.tag.entity.PlaceTag;
import com.sikhye.chabak.service.tag.entity.PostingTag;
import com.sikhye.chabak.service.tag.repository.PlaceTagRepository;
import com.sikhye.chabak.service.tag.repository.PostingTagRepository;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TagServiceImplTest {

	@Autowired
	private TagService tagService;

	@Autowired
	private PlaceTagRepository placeTagRepository;

	@Autowired
	private PostingTagRepository postingTagRepository;

	@Test
	@DisplayName("001. 장소 태그 조회")
	@Order(1)
	public void findPlaceTagsTest() {
		//given
		List<String> placeTags1 = tagService.findPlaceTags(1L);
		List<String> placeTags2 = tagService.findPlaceTags(2L);

		//when
		int size1 = placeTags1.size();
		int size2 = placeTags2.size();

		//then
		assertEquals(size1, 4);
		assertEquals(size2, 4);
	}

	@Test
	@DisplayName("002. 장소 태그 등록")
	@Order(2)
	public void addPlaceTagsTest() {
		//given
		PlaceTagReq placeTagReq = PlaceTagReq.builder()
			.placeTags(List.of("태그1", "태그2"))
			.build();

		//when
		// "태그1" 이 있는지 검사
		long count = tagService.addPlaceTags(1L, placeTagReq).stream()
			.map(PlaceTagRes::getPlaceTagName)
			.filter(s -> s.equals("태그1"))
			.count();

		//then
		assertEquals(count, 1);
	}

	@Test
	@DisplayName("003. 장소 태그 수정")
	@Order(3)
	public void editPlaceTagTest() {
		//given
		Long tagId = tagService.editPlaceTag(1L, 1L, "태그1");

		//when
		PlaceTag placeTag = placeTagRepository.findById(tagId)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(placeTag.getName(), "태그1");
	}

	@Test
	@DisplayName("004. 장소 태그 삭제")
	@Order(4)
	public void deletePlaceTagTest() {
		//given
		Long tagId = tagService.placeTagStatusToDelete(1L, 1L);

		//when
		PlaceTag placeTag = placeTagRepository.findById(tagId)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(placeTag.getStatus(), DELETED);
	}

	@Test
	@DisplayName("005. 포스팅 태그 조회")
	@Order(5)
	public void findPostingTagsTest() {
		//given
		List<String> postingTags1 = tagService.findPostingTags(1L);
		List<String> postingTags2 = tagService.findPostingTags(2L);

		//when
		int size1 = postingTags1.size();
		int size2 = postingTags2.size();

		//then
		assertEquals(size1, 3);
		assertEquals(size2, 0);
	}

	@Test
	@DisplayName("006. 포스팅 태그 등록")
	@Order(6)
	public void addPostingTagsTest() {
		//given
		PostingTagReq postingTagReq = PostingTagReq.builder()
			.postingTags(List.of("태그1", "태그2"))
			.build();

		//when
		// "태그1" 이 있는지 검사
		long count = tagService.addPostingTags(1L, postingTagReq).stream()
			.map(PostingTagRes::getPostingTagName)
			.filter(s -> s.equals("태그1"))
			.count();

		//then
		assertEquals(count, 1);
	}

	@Test
	@DisplayName("007. 포스팅 태그 수정")
	@Order(7)
	public void editPostingTagTest() {
		//given
		Long tagId = tagService.editPostingTag(1L, 1L, "태그1");

		//when
		PostingTag postingTag = postingTagRepository.findById(tagId)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(postingTag.getName(), "태그1");
	}

	@Test
	@DisplayName("008. 포스팅 태그 삭제")
	@Order(8)
	public void deletePostingTagTest() {
		//given
		Long tagId = tagService.postingTagStatusToDelete(1L, 1L);

		//when
		PostingTag postingTag = postingTagRepository.findById(tagId)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(postingTag.getStatus(), DELETED);
	}

}
