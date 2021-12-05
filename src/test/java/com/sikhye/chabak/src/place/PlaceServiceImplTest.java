package com.sikhye.chabak.src.place;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static com.sikhye.chabak.global.time.BaseStatus.*;
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
import com.sikhye.chabak.service.comment.dto.CommentRes;
import com.sikhye.chabak.service.place.PlaceService;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceSearchRes;
import com.sikhye.chabak.service.place.entity.Place;
import com.sikhye.chabak.service.place.repository.PlaceImageRepository;
import com.sikhye.chabak.service.place.repository.PlaceRepository;

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
	private PlaceImageRepository placeImageRepository;

	@PersistenceContext
	private EntityManager em;

	@AfterAll
	public void deleteMemberTable() {
		Optional<Place> findPlace = placeRepository.findById(1L);
		findPlace.ifPresent(Place::setStatusToUsed);
	}

	@Test
	@DisplayName("001. 장소 불러오기")
	@Order(1)
	public void getPlaceTest() {
		//given
		PlaceDetailRes place = placeService.getPlace(3L);

		//when
		String placeName = place.getName();
		System.out.println("place.getTagNames() = " + place.getTagNames());

		CommentRes commentRes = place.getCommentResList().get(0);
		System.out.println("placeReviewRes = " + commentRes.getName());
		System.out.println("placeReviewRes.getContent() = " + commentRes.getContent());
		System.out.println("placeReviewRes.getWritingDate() = " + commentRes.getWritingDate());

		//then
		assertEquals(placeName, "옥정호차박지");
		assertEquals(place.getReviewCount(), 1L);
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
		Place findPlace = placeRepository.findPlaceByIdAndStatus(1L, used)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(updatePlace, 1L);
		assertEquals(findPlace.getLatitude(), 12.34);
		assertEquals(findPlace.getLongitude(), 56.78);

	}

	@Test
	@DisplayName("003. 장소 삭제하기")
	@Order(3)
	public void deletePlaceTest() {
		//given
		placeService.statusToDelete(1L);

		//when
		Optional<Place> findPlace = placeRepository.findById(1L);

		//then
		findPlace.ifPresent(place -> assertEquals(place.getStatus(), deleted));
	}

	@Test
	@DisplayName("004. 주변장소 조회")
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

}

