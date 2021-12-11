package com.sikhye.chabak.src.place;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.AfterAll;
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
import com.sikhye.chabak.service.place.PlaceService;
import com.sikhye.chabak.service.place.dto.PlaceCommentRes;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceSearchRes;
import com.sikhye.chabak.service.place.dto.PlaceTagReq;
import com.sikhye.chabak.service.place.dto.PlaceTagRes;
import com.sikhye.chabak.service.place.entity.Place;
import com.sikhye.chabak.service.place.entity.PlaceTag;
import com.sikhye.chabak.service.place.repository.PlaceCommentRepository;
import com.sikhye.chabak.service.place.repository.PlaceRepository;
import com.sikhye.chabak.service.place.repository.PlaceTagRepository;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlaceServiceImplTest {

	@Autowired
	private PlaceService placeService;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private PlaceTagRepository placeTagRepository;

	@Autowired
	private PlaceCommentRepository placeCommentRepository;

	@PersistenceContext
	private EntityManager em;

	@AfterAll
	public void deleteMemberTable() {
		Optional<Place> findPlace = placeRepository.findById(1L);
		findPlace.ifPresent(Place::setStatusToUsed);
	}

	@Test
	@DisplayName("01. 장소 불러오기")
	@Order(1)
	public void getPlaceTest() {
		//given
		PlaceDetailRes place = placeService.getPlace(3L);

		//when
		String placeName = place.getName();
		System.out.println("place.getTagNames() = " + place.getTagNames());

		PlaceCommentRes commentRes = place.getCommentResList().get(0);
		System.out.println("placeReviewRes = " + commentRes.getName());
		System.out.println("placeReviewRes.getContent() = " + commentRes.getContent());
		System.out.println("placeReviewRes.getWritingDate() = " + commentRes.getWritingDate());

		//then
		assertEquals(placeName, "옥정호차박지");
		assertEquals(place.getReviewCount(), 2L);
		assertEquals(place.getImageCount(), 2L);
	}

	@Test
	@DisplayName("002. 장소 좌표값 설정")
	@Order(2)
	public void setPointTest() {
		//given
		Long updatePlace = placeService.savePoint(1L, 12.34, 56.78);
		em.flush();
		em.clear();

		//when
		Place findPlace = placeRepository.findPlaceByIdAndStatus(1L, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(updatePlace, 1L);
		assertEquals(findPlace.getLatitude(), 12.34);
		assertEquals(findPlace.getLongitude(), 56.78);

	}

	@Test
	@DisplayName("03. 장소 삭제하기")
	@Order(3)
	public void deletePlaceTest() {
		//given
		placeService.statusToDelete(1L);

		//when
		Optional<Place> findPlace = placeRepository.findById(1L);

		//then
		findPlace.ifPresent(place -> assertEquals(place.getStatus(), DELETED));
	}

	@Test
	@DisplayName("04. 주변장소 조회")
	@Order(4)
	public void aroundPlaceTest() {
		//given
		List<PlaceSearchRes> placeSearchRes = placeService.aroundPlace(35.64317357, 127.14203310, 1D);

		//when
		System.out.println("========================");
		placeSearchRes.stream()
			.map(PlaceSearchRes::getPlaceId)
			.forEach(System.out::println);

		System.out.println("========================");
		placeSearchRes.stream()
			.map(PlaceSearchRes::getDistance)
			.forEach(System.out::println);
		System.out.println("========================");

		//then
	}

	@Test
	@DisplayName("05. 장소 태그 조회")
	@Order(5)
	public void findPlaceTagsTest() {
		//given
		List<String> placeTags1 = placeService.findPlaceTags(1L);
		List<String> placeTags2 = placeService.findPlaceTags(2L);

		//when
		int size1 = placeTags1.size();
		int size2 = placeTags2.size();

		//then
		assertEquals(size1, 2);
		assertEquals(size2, 2);
	}

	@Test
	@DisplayName("06. 장소 태그 등록")
	@Order(6)
	public void addPlaceTagsTest() {
		//given
		PlaceTagReq placeTagReq = PlaceTagReq.builder()
			.placeTags(List.of("태그1", "태그2"))
			.build();

		//when
		// "태그1" 이 있는지 검사
		long count = placeService.addPlaceTags(1L, placeTagReq).stream()
			.map(PlaceTagRes::getPlaceTagName)
			.filter(s -> s.equals("태그1"))
			.count();

		//then
		assertEquals(count, 1);
	}

	@Test
	@DisplayName("07. 장소 태그 수정")
	@Order(7)
	public void editPlaceTagTest() {
		//given
		Long tagId = placeService.editPlaceTag(1L, 1L, "고요한");

		//when
		PlaceTag placeTag = placeTagRepository.findById(tagId)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(placeTag.getName(), "고요한");
	}

	@Test
	@DisplayName("08. 장소 태그 삭제")
	@Order(8)
	public void deletePlaceTagTest() {
		//given
		Long tagId = placeService.placeTagStatusToDelete(1L, 1L);

		//when
		PlaceTag placeTag = placeTagRepository.findById(tagId)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(placeTag.getStatus(), DELETED);
	}

	@Test
	@DisplayName("09. 장소 댓글 조회")
	@Order(9)
	public void findPlaceCommentsTest() {
		//given
		List<PlaceCommentRes> placeComments = placeService.findPlaceComments(1L);

		//when

		//then
		assertEquals(placeComments.size(), 4);

	}

	// JWT 토큰 필요, MockMVC 이용
	// @Test
	// @DisplayName("10. 장소 댓글 작성")
	// @Order(10)
	// public void addPlaceCommentTest() {
	// 	//given
	// 	Long commentId = placeService.addPlaceComment(1L, new PlaceCommentReq("장소 댓글 테스트"));
	//
	// 	//when
	// 	PlaceComment placeReview = placeCommentRepository.findPlaceCommentByIdAndStatus(commentId, USED);
	//
	// 	//then
	// 	assertEquals(placeReview.getContent(), "장소 댓글 테스트");
	// }
	//
	// @Test
	// @DisplayName("11. 장소 댓글 수정")
	// @Order(11)
	// public void editPlaceCommentTest() {
	// 	//given
	// 	Long commentId = placeService.addPlaceComment(1L, new PlaceCommentReq("장소 댓글 테스트"));
	// 	PlaceComment placeReview = placeCommentRepository.findPlaceCommentByIdAndStatus(commentId, USED);
	// 	Long editCommentId = placeService.editPlaceComment(1L, placeReview.getId(),
	// 		new PlaceCommentReq("수정된 장소 댓글 테스트"));
	//
	// 	em.flush();
	// 	em.clear();
	//
	// 	//when
	// 	PlaceComment findPlaceReview = placeCommentRepository.findPlaceCommentByIdAndStatus(editCommentId, USED);
	//
	// 	//then
	// 	assertEquals(findPlaceReview.getContent(), "수정된 장소 댓글 테스트");
	// }
	//
	// @Test
	// @DisplayName("12. 장소 댓글 삭제")
	// @Order(12)
	// public void deletePlaceCommentTest() {
	// 	//given
	// 	Long deletedId = placeService.statusToDeletePlaceComment(1L, 1L);
	//
	// 	em.flush();
	// 	em.clear();
	//
	// 	//when
	// 	PlaceComment deletedReview = placeCommentRepository.findById(deletedId)
	// 		.orElseThrow(() -> new BaseException(DELETE_EMPTY));
	//
	// 	//then
	// 	assertEquals(deletedReview.getStatus(), DELETED);
	//
	// }

}

