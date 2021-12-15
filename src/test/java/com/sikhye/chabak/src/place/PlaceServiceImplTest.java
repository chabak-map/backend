package com.sikhye.chabak.src.place;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.place.PlaceService;
import com.sikhye.chabak.service.place.dto.PlaceAroundRes;
import com.sikhye.chabak.service.place.dto.PlaceCommentReq;
import com.sikhye.chabak.service.place.dto.PlaceCommentRes;
import com.sikhye.chabak.service.place.dto.PlaceDetailRes;
import com.sikhye.chabak.service.place.dto.PlaceRankRes;
import com.sikhye.chabak.service.place.dto.PlaceTagReq;
import com.sikhye.chabak.service.place.dto.PlaceTagRes;
import com.sikhye.chabak.service.place.entity.Place;
import com.sikhye.chabak.service.place.entity.PlaceComment;
import com.sikhye.chabak.service.place.entity.PlaceTag;
import com.sikhye.chabak.service.place.repository.PlaceCommentRepository;
import com.sikhye.chabak.service.place.repository.PlaceRepository;
import com.sikhye.chabak.service.place.repository.PlaceTagRepository;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class PlaceServiceImplTest {

	@Autowired
	private PlaceService placeService;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private PlaceTagRepository placeTagRepository;

	@Autowired
	private PlaceCommentRepository placeCommentRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private EntityManager em;

	ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
			.alwaysDo(print())
			.build();
	}

	@AfterAll
	public void deleteMemberTable() {
		Optional<Place> findPlace = placeRepository.findById(1L);
		findPlace.ifPresent(Place::setStatusToUsed);
	}

	@Test
	@DisplayName("07. 장소 상세정보 조회")
	@Order(1)
	public void getPlaceTest() {
		//given
		PlaceDetailRes place = placeService.getPlace(3L);

		//when
		String placeName = place.getName();

		//then
		assertEquals(placeName, "옥정호차박지");
		assertEquals(place.getReviewCount(), 3L);
		assertEquals(place.getImageCount(), 2L);
	}

	@Test
	@DisplayName("08. 주변장소 반경조회")
	@Order(2)
	public void aroundPlaceTest() {
		//given
		Double latitude = 35.64317357D;
		Double longitude = 127.14203310D;
		Double radius = 1D;

		//when
		List<PlaceAroundRes> placeSearchRes = placeService.aroundPlace(latitude, longitude, radius);

		System.out.println("========================");
		placeSearchRes.stream()
			.map(PlaceAroundRes::getPlaceId)
			.forEach(System.out::println);

		System.out.println("========================");
		placeSearchRes.stream()
			.map(PlaceAroundRes::getDistance)
			.forEach(System.out::println);
		System.out.println("========================");

		//then
		assertEquals(1L, placeSearchRes.size());
	}

	@Test
	@DisplayName("11-1. 장소 삭제하기")
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
	@DisplayName("11-2. 일반유저가 장소 삭제 시도")
	@Order(4)
	public void unAuthorizedDeletePlaceTest() {
		//given
		long placeId = 1L;

		//when
		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(patch("/places/" + placeId + "/status")
					.header("X-ACCESS-TOKEN",
						"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn());

	}

	@Test
	@DisplayName("12-1. 장소 좌표 저장")
	@Order(5)
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
	@DisplayName("12-2. 일반유저 장소 좌표 저장 시도")
	@Order(6)
	public void unAuthorizedSetPointTest() {
		//given
		long placeId = 1L;
		Double latitude = 12.34D;
		Double longitude = 56.78D;

		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(post("/places/" + placeId)
					.param("lat", String.valueOf(latitude))
					.param("lng", String.valueOf(longitude))
					.header("X-ACCESS-TOKEN",
						"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.code", 2003).exists())
				.andReturn());

	}

	@Test
	@DisplayName("13. 장소 태그 조회")
	@Order(7)
	public void findPlaceTagsTest() {
		//given
		List<String> placeTags1 = placeService.findPlaceTags(1L);
		List<String> placeTags2 = placeService.findPlaceTags(2L);

		//when
		int size1 = placeTags1.size();
		int size2 = placeTags2.size();

		//then
		assertEquals(size1, 0);
		assertEquals(size2, 2);
	}

	@Test
	@DisplayName("14-1. 장소 태그 등록")
	@Order(8)
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
	@DisplayName("14-2. 일반 유저 장소 태그 등록 시도")
	@Order(9)
	public void unAuthorizedAddPlaceTagsTest() {
		//given
		long placeId = 1L;
		PlaceTagReq placeTagReq = PlaceTagReq.builder()
			.placeTags(List.of("태그1", "태그2"))
			.build();

		//when
		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(post("/places/" + placeId + "/tags")
					.header("X-ACCESS-TOKEN",
						"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(placeTagReq)))
				.andDo(print())
				.andExpect(jsonPath("$.code", 2003).exists())
				.andReturn());
	}

	@Test
	@DisplayName("15-1. 장소 태그 수정")
	@Order(10)
	public void editPlaceTagTest() {
		//given
		Long tagId = placeService.editPlaceTag(2L, 3L, "고요한");

		//when
		PlaceTag placeTag = placeTagRepository.findById(tagId)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(placeTag.getName(), "고요한");
	}

	@Test
	@DisplayName("15-2. 일반 유저 장소 태그 수정 시도")
	@Order(11)
	public void unAuthorizedEditPlaceTagTest() {
		//given
		long placeId = 2L;
		long tagId = 3L;

		//when
		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(patch("/places/" + placeId + "/tags/" + tagId)
					.header("X-ACCESS-TOKEN",
						"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
					.contentType(MediaType.APPLICATION_JSON)
					.param("placeTagName", "고요한"))
				.andDo(print())
				.andExpect(jsonPath("$.code", 2003).exists())
				.andReturn());
	}

	@Test
	@DisplayName("16-1. 장소 태그 삭제")
	@Order(12)
	public void deletePlaceTagTest() {
		//given
		Long tagId = placeService.placeTagStatusToDelete(2L, 3L);

		//when
		PlaceTag placeTag = placeTagRepository.findById(tagId)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(placeTag.getStatus(), DELETED);
	}

	@Test
	@DisplayName("16-2. 일반 유저 장소 태그 삭제 시도")
	@Order(13)
	public void unAuthorizedDeletePlaceTagTest() {
		//given
		long placeId = 2L;
		long placeTagId = 3L;

		//when
		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(patch("/places/" + placeId + "/tags/" + placeTagId + "/status")
					.header("X-ACCESS-TOKEN",
						"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.code", 2003).exists())
				.andReturn());
	}

	@Test
	@DisplayName("28. 장소 댓글 조회")
	@Order(14)
	public void findPlaceCommentsTest() {
		//given
		List<PlaceCommentRes> placeComments = placeService.findPlaceComments(1L);

		//then
		assertEquals(placeComments.size(), 4);

	}

	// JWT 토큰 필요, MockMVC 이용
	@Test
	@DisplayName("29. 장소 댓글 작성")
	@Order(15)
	public void addPlaceCommentTest() throws Exception {
		//given
		long placeId = 1L;
		PlaceCommentReq placeCommentReq = PlaceCommentReq.builder()
			.content("테스트 장소 댓글")
			.build();

		//when
		MvcResult mvcResult = this.mockMvc.perform(post("/places/" + placeId + "/comments")
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(placeCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		// >> ptpt: contains, MockMVC를 이용해서 문자열 일부 비교하는 방법
		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number findId = JsonPath.read(contentAsString, "$.result");

		PlaceComment placeComment = placeCommentRepository.findPlaceCommentByIdAndStatus(findId.longValue(), USED);

		//then
		assertThat(placeComment.getContent(), containsString(placeCommentReq.getContent()));
	}

	@Test
	@DisplayName("30-1. 장소 댓글 수정")
	@Order(16)
	public void editPlaceCommentTest() throws Exception {
		//given
		long placeId = 1L;
		PlaceCommentReq placeCommentReq = PlaceCommentReq.builder()
			.content("장소 댓글 테스트")
			.build();

		PlaceCommentReq modifiedPlaceCommentReq = PlaceCommentReq.builder()
			.content("수정된 장소 댓글 테스트")
			.build();

		// LocalDateTime nw = LocalDateTime.now();
		// String modifiedPlaceCommentJson = "{'content':'수정된 장소 댓글 테스트', 'created_at':'" + nw + "'}";

		// 댓글 작성
		MvcResult mvcResult = this.mockMvc.perform(post("/places/" + placeId + "/comments")
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(modifiedPlaceCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		// when
		// 댓글 ID 획득
		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number commentId = JsonPath.read(contentAsString, "$.result");

		// 댓글 수정
		this.mockMvc.perform(patch("/places/" + placeId + "/comments/" + commentId.longValue())
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(modifiedPlaceCommentReq))
			)
			.andDo(print())
			.andReturn();

		PlaceComment placeComment = placeCommentRepository.findPlaceCommentByIdAndStatus(
			commentId.longValue(), USED);

		//then
		assertEquals(placeComment.getContent(), modifiedPlaceCommentReq.getContent());
	}

	@Test
	@DisplayName("30-2. 권한 없는 사용자 - 장소 댓글 수정")
	@Order(17)
	public void unAuthorizedEditPlaceCommentTest() throws Exception {
		//given
		long placeId = 1L;
		PlaceCommentReq placeCommentReq = PlaceCommentReq.builder()
			.content("장소 댓글 테스트")
			.build();

		PlaceCommentReq modifiedPlaceCommentReq = PlaceCommentReq.builder()
			.content("수정된 장소 댓글 테스트")
			.build();

		// 댓글 작성
		MvcResult mvcResult = this.mockMvc.perform(post("/places/" + placeId + "/comments")
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(placeCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		// when
		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number commentId = JsonPath.read(contentAsString, "$.result");

		//then
		// 획득한 댓글 ID에 대해 다른 계정으로 수정 시도
		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(patch("/places/" + placeId + "/comments/" + commentId)
					.header("X-ACCESS-TOKEN",
						"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA5LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MjA2NTE5LCJleHAiOjE2NzA3NDI1MTl9.m0h08jwzxsYzpXHS51wqhPWYgWYjmwmLcGFDVOFKE5o")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(modifiedPlaceCommentReq)))
				.andDo(print())
				.andReturn());
	}

	@Test
	@DisplayName("31-1. 장소 댓글 삭제")
	@Order(18)
	public void deletePlaceCommentTest() throws Exception {
		//given
		long placeId = 1L;
		PlaceCommentReq placeCommentReq = PlaceCommentReq.builder()
			.content("장소 댓글 테스트")
			.build();

		// 댓글 작성
		MvcResult mvcResult = this.mockMvc.perform(post("/places/" + placeId + "/comments")
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(placeCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		//when
		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number commentId = JsonPath.read(contentAsString, "$.result");

		// 본인 글 삭제 시도
		MvcResult mvcResult2 = this.mockMvc.perform(
				patch("/places/" + placeId + "/comments/" + commentId.longValue() + "/status")
					.header("X-ACCESS-TOKEN",
						"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(placeCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		//then
		assertNotNull(placeCommentRepository.findPlaceCommentByIdAndStatus(commentId.longValue(), DELETED));

	}

	@Test
	@DisplayName("31-2. 권한 없는 사용자 - 장소 댓글 삭제")
	@Order(19)
	public void unAuthorizedDeletePlaceCommentTest() throws Exception {
		//given
		long placeId = 1L;
		PlaceCommentReq placeCommentReq = PlaceCommentReq.builder()
			.content("장소 댓글 테스트")
			.build();

		// 댓글 작성
		MvcResult mvcResult = this.mockMvc.perform(post("/places/" + placeId + "/comments")
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(placeCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		//when
		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number commentId = JsonPath.read(contentAsString, "$.result");

		// 권한 없는 삭제 시도
		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(patch("/places/" + placeId + "/comments/" + commentId.longValue())
					.header("X-ACCESS-TOKEN",
						"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA5LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MjA2NTE5LCJleHAiOjE2NzA3NDI1MTl9.m0h08jwzxsYzpXHS51wqhPWYgWYjmwmLcGFDVOFKE5o")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(placeCommentReq)))
				.andDo(print())
				.andReturn());
	}

	@Test
	@DisplayName("10. 차박지 랭킹 (1-5위)")
	@Order(20)
	public void getTop5PlaceRanksTest() {
		//given

		//when
		List<PlaceRankRes> top5PlaceRanks = placeService.getTop5PlaceRanks();

		//then
		System.out.println("Complete Test");
	}

}

