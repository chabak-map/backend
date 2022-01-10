package com.sikhye.chabak.src.bookmark;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityManager;

import org.junit.Before;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sikhye.chabak.service.bookmark.BookmarkService;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class BookmarkServiceImplTest {

	@Autowired
	private BookmarkService bookmarkService;

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

	@Test
	@DisplayName("43. 북마크 조회 테스트")
	@Order(1)
	public void findBookmarkTest() throws Exception {
		//given

		//when
		MvcResult mvcResult = this.mockMvc.perform(get("/bookmarks")
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(jsonPath("$.result..id", 1).exists())
			.andReturn();

		System.out.println(
			"mvcResult.getResponse().getContentAsString() = " + mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("44. 북마크 등록 테스트")
	@Order(2)
	public void registerBookmarkTest() throws Exception {
		//given
		long placeId = 3L;

		//when
		MvcResult mvcResult = this.mockMvc.perform(post("/bookmarks/" + placeId)
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(jsonPath("$.result", 6).exists())
			.andReturn();

		//then
	}

	@Test
	@DisplayName("45. 북마크 삭제 테스트")
	@Order(3)
	public void statusToDeleteBookmarkTest() throws Exception {
		//given
		long bookmarkId = 5L;

		//when
		MvcResult mvcResult = this.mockMvc.perform(patch("/bookmarks/" + bookmarkId + "/status")
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(jsonPath("$.result", 5).exists())
			.andReturn();

		//then
	}

}
