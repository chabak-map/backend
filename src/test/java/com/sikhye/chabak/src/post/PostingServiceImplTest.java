package com.sikhye.chabak.src.post;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.post.PostingService;
import com.sikhye.chabak.service.post.dto.PostingCommentReq;
import com.sikhye.chabak.service.post.dto.PostingCommentRes;
import com.sikhye.chabak.service.post.dto.PostingDetailRes;
import com.sikhye.chabak.service.post.dto.PostingRecentRes;
import com.sikhye.chabak.service.post.dto.PostingReq;
import com.sikhye.chabak.service.post.dto.PostingRes;
import com.sikhye.chabak.service.post.dto.PostingTagReq;
import com.sikhye.chabak.service.post.dto.PostingTagRes;
import com.sikhye.chabak.service.post.entity.Posting;
import com.sikhye.chabak.service.post.entity.PostingComment;
import com.sikhye.chabak.service.post.entity.PostingTag;
import com.sikhye.chabak.service.post.repository.PostingCommentRepository;
import com.sikhye.chabak.service.post.repository.PostingRepository;
import com.sikhye.chabak.service.post.repository.PostingTagRepository;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class PostingServiceImplTest {

	@Autowired
	private PostingService postingService;

	@Autowired
	private PostingRepository postingRepository;

	@Autowired
	private PostingTagRepository postingTagRepository;

	@Autowired
	private PostingCommentRepository postingCommentRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private EntityManager em;

	ObjectMapper objectMapper = new ObjectMapper();

	private final String testUserJwt = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA3LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MTg0OTI3LCJleHAiOjE2Mzk1MzA1Mjd9.tc-Y9Gsy_hM-igUqYmhpnrC_u8E-Lyp_DQgV7hrnd9Y";
	private final String anotherUserJwt = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTA5LCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MjA2NTE5LCJleHAiOjE2NzA3NDI1MTl9.m0h08jwzxsYzpXHS51wqhPWYgWYjmwmLcGFDVOFKE5o";

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
			.alwaysDo(print())
			.build();
	}

	@Test
	@DisplayName("17. 포스트 태그 조회")
	@Order(1)
	public void findPlaceTagsTest() {
		//given
		List<String> postingTags1 = postingService.findPostingTags(1L);
		List<String> postingTags2 = postingService.findPostingTags(2L);

		//when
		int size1 = postingTags1.size();
		int size2 = postingTags2.size();

		//then
		assertEquals(size1, 2);
		assertEquals(size2, 2);
	}

	@Test
	@DisplayName("18-1. 포스팅 태그 등록")
	@Order(2)
	public void addPostingTagsTest() {
		//given
		PostingTagReq postingTagReq = PostingTagReq.builder()
			.postingTags(List.of("태그1", "태그2"))
			.build();

		//when
		// "태그1" 이 있는지 검사
		long count = postingService.addPostingTags(1L, postingTagReq).stream()
			.map(PostingTagRes::getPostingTagName)
			.filter(s -> s.equals("태그1"))
			.count();

		//then
		assertEquals(count, 1);
	}

	@Test
	@DisplayName("18-2. 권한 없는 유저 포스팅 태그 등록 시도")
	@Order(3)
	public void unAuthorizedAddPostingTagsTest() {
		//given
		long postId = 1L;
		PostingTagReq postingTagReq = PostingTagReq.builder()
			.postingTags(List.of("태그1", "태그2"))
			.build();

		//when
		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(post("/posts/" + postId + "/tags")
					.header("X-ACCESS-TOKEN",
						anotherUserJwt)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(postingTagReq)))
				.andDo(print())
				.andExpect(jsonPath("$.code", 2003).exists())
				.andReturn());
	}

	@Test
	@DisplayName("19-1. 포스팅 태그 수정")
	@Order(4)
	public void editPostingTagTest() {
		//given
		Long tagId = postingService.editPostingTag(1L, 1L, "태그1");

		//when
		PostingTag postingTag = postingTagRepository.findById(tagId)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(postingTag.getName(), "태그1");
	}

	@Test
	@DisplayName("19-2. 권한 없는 사용자의 포스팅 태그 수정")
	@Order(5)
	public void unAuthorizedEditPostingTagTest() {
		//given
		long postId = 1L;
		long tagId = 1L;

		//when
		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(patch("/posts/" + postId + "/tags/" + tagId)
					.header("X-ACCESS-TOKEN",
						anotherUserJwt)
					.contentType(MediaType.APPLICATION_JSON)
					.param("tagName", "수정된 태그1"))
				.andDo(print())
				.andExpect(jsonPath("$.code", 2003).exists())
				.andReturn());
	}

	@Test
	@DisplayName("20-1. 포스팅 태그 삭제")
	@Order(6)
	public void deletePostingTagTest() {
		//given
		Long tagId = postingService.postingTagStatusToDelete(1L, 1L);

		//when
		PostingTag postingTag = postingTagRepository.findById(tagId)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_PLACE));

		//then
		assertEquals(postingTag.getStatus(), DELETED);
	}

	@Test
	@DisplayName("20-2. 권한 없는 사용자 - 포스팅 태그 삭제")
	@Order(7)
	public void unAuthDeletePostingTagTest() {
		//given
		long postId = 1L;
		long tagId = 1L;

		//when
		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(patch("/posts/" + postId + "/tags/" + tagId + "/status")
					.header("X-ACCESS-TOKEN",
						anotherUserJwt)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.code", 2003).exists())
				.andReturn());
	}

	@Test
	@DisplayName("21. 포스팅 전체 조회 테스트")
	@Order(8)
	public void findPostingsTest() {
		//given
		List<PostingRes> posts = postingService.findPosts();

		//when

		//then
		assertEquals(posts.size(), 3);
	}

	@Test
	@DisplayName("22. 멤버가 작성한 포스팅 조회 테스트")
	@Order(9)
	public void findMyPostingsTest() throws Exception {
		//given

		//when
		MvcResult mvcResult = this.mockMvc.perform(get("/posts" + "/me")
				.header("X-ACCESS-TOKEN", testUserJwt)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		// >> ptpt: contains, MockMVC를 이용해서 문자열 일부 비교하는 방법
		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number postId = JsonPath.read(contentAsString, "$.result.[?(@.title == '테스트1')]");

		Posting findPost = postingRepository.findPostingByIdAndStatus(postId.longValue(), USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_POST));

		//then
		assertThat(findPost.getContent(), containsString("테스트1"));
	}

	@Test
	@DisplayName("23. 포스팅 등록")
	@Order(10)
	public void createPostTest() throws Exception {
		//given
		PostingReq postingReq = PostingReq.builder()
			.content("포스팅테스트1")
			.title("포스팅1")
			.build();

		//when
		MvcResult mvcResult = this.mockMvc.perform(post("/posts")
				.header("X-ACCESS-TOKEN", testUserJwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postingReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number postId = JsonPath.read(contentAsString, "$.result");

		Posting findPost = postingRepository.findPostingByIdAndStatus(postId.longValue(), USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_POST));

		//then
		assertEquals(findPost.getContent(), postingReq.getContent());
		assertEquals(findPost.getTitle(), postingReq.getTitle());
	}

	@Test
	@DisplayName("24. 포스트 상세 조회 테스트")
	@Order(11)
	public void findPostDetailTest() {
		//given
		PostingDetailRes postDetail = postingService.findPostDetail(1L);

		//when
		List<String> postImageUrls = postDetail.getPostImageUrls();

		long count = postImageUrls.stream()
			.filter(s -> s.equals("https://univ-city-bucket.s3.ap-northeast-2.amazonaws.com/images/posting/1/1-1.png"))
			.count();

		//then
		assertEquals(postDetail.getNickname(), "mockuser1");
		assertEquals(count, 1L);
	}

	// @Test
	// @DisplayName("25-1. 포스트 삭제 테스트")
	// @Order(12)
	// public void statusToDeletePostTest() {
	// 	//given
	// 	PostingDetailRes postDetail = postingService.findPostDetail(1L);
	//
	// 	//when
	// 	List<String> postImageUrls = postDetail.getPostImageUrls();
	//
	// 	long count = postImageUrls.stream()
	// 		.filter(s -> s.equals("https://univ-city-bucket.s3.ap-northeast-2.amazonaws.com/images/posting/1/1-1.png"))
	// 		.count();
	//
	// 	//then
	// 	assertEquals(postDetail.getNickname(), "mockuser1");
	// 	assertEquals(count, 1L);
	// }
	//
	// @Test
	// @DisplayName("25-2. 권한 없는 포스트 삭제 테스트")
	// @Order(13)
	// public void unAuthorizedStatusToDeletePostTest() {
	// 	//given
	// 	PostingDetailRes postDetail = postingService.findPostDetail(1L);
	//
	// 	//when
	// 	List<String> postImageUrls = postDetail.getPostImageUrls();
	//
	// 	long count = postImageUrls.stream()
	// 		.filter(s -> s.equals("https://univ-city-bucket.s3.ap-northeast-2.amazonaws.com/images/posting/1/1-1.png"))
	// 		.count();
	//
	// 	//then
	// 	assertEquals(postDetail.getNickname(), "mockuser1");
	// 	assertEquals(count, 1L);
	// }

	@Test
	@DisplayName("32. 포스트 댓글 리스트 조회")
	@Order(12)
	public void findPostCommentsTest() {
		//given
		List<PostingCommentRes> postComments = postingService.findPostComments(1L);

		//when

		//then
		assertEquals(postComments.size(), 1);
	}

	@Test
	@DisplayName("33. 포스트 댓글 작성")
	@Order(13)
	public void addPostCommentTest() throws Exception {
		//given
		long postId = 1L;
		PostingCommentReq postingCommentReq = PostingCommentReq.builder()
			.content("포스트 테스트 댓글입니다")
			.build();

		//when
		MvcResult mvcResult = this.mockMvc.perform(post("/posts/" + postId + "/comments")
				.header("X-ACCESS-TOKEN", testUserJwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postingCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number postCommentId = JsonPath.read(contentAsString, "$.result");

		PostingComment findPostingComment = postingCommentRepository.findPostingCommentByIdAndStatus(
			postCommentId.longValue(), USED);

		//then
		assertEquals(postingCommentReq.getContent(), findPostingComment.getContent());
	}

	@Test
	@DisplayName("34. 포스트 댓글 수정")
	@Order(14)
	public void editPostCommentTest() throws Exception {
		//given
		long postId = 1L;
		PostingCommentReq postingCommentReq = PostingCommentReq.builder()
			.content("포스트 테스트 댓글입니다")
			.build();

		PostingCommentReq editPostingCommentReq = PostingCommentReq.builder()
			.content("수정된 포스트 테스트 댓글입니다")
			.build();

		//when
		// 포스트 댓글 작성
		MvcResult mvcResult = this.mockMvc.perform(post("/posts/" + postId + "/comments")
				.header("X-ACCESS-TOKEN", testUserJwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postingCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number postCommentId = JsonPath.read(contentAsString, "$.result");

		// 포스트 댓글 수정
		MvcResult mvcResult2 = this.mockMvc.perform(patch("/posts/" + postId + "/comments/" + postCommentId)
				.header("X-ACCESS-TOKEN", testUserJwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(editPostingCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		PostingComment postingComment = postingCommentRepository.findPostingCommentByIdAndStatus(
			postCommentId.longValue(), USED);

		//then
		assertEquals(postingComment.getContent(), editPostingCommentReq.getContent());
	}

	@Test
	@DisplayName("35. 권한없는 사용자 - 포스트 댓글 수정")
	@Order(15)
	public void unAuthorizedEditPostCommentTest() throws Exception {
		//given
		long postId = 1L;
		PostingCommentReq postingCommentReq = PostingCommentReq.builder()
			.content("포스트 테스트 댓글입니다")
			.build();

		PostingCommentReq editPostingCommentReq = PostingCommentReq.builder()
			.content("수정된 포스트 테스트 댓글입니다")
			.build();

		//when
		// 포스트 댓글 작성
		MvcResult mvcResult = this.mockMvc.perform(post("/posts/" + postId + "/comments")
				.header("X-ACCESS-TOKEN", testUserJwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postingCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number postCommentId = JsonPath.read(contentAsString, "$.result");

		//then
		// 포스트 댓글 수정
		assertThrows(NestedServletException.class, () ->
			this.mockMvc.perform(patch("/posts/" + postId + "/comments/" + postCommentId)
					.header("X-ACCESS-TOKEN", anotherUserJwt)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(editPostingCommentReq)))
				.andDo(print())
				.andReturn());
	}

	@Test
	@DisplayName("36. 포스트 댓글 삭제")
	@Order(16)
	public void deletePostCommentTest() throws Exception {
		//given
		long postId = 1L;
		PostingCommentReq postingCommentReq = PostingCommentReq.builder()
			.content("포스트 테스트 댓글입니다")
			.build();

		//when
		MvcResult mvcResult = this.mockMvc.perform(post("/posts/" + postId + "/comments")
				.header("X-ACCESS-TOKEN", testUserJwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postingCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number postCommentId = JsonPath.read(contentAsString, "$.result");

		// 포스트 댓글 삭제
		MvcResult mvcResult2 = this.mockMvc.perform(patch("/posts/" + postId + "/comments/" + postCommentId + "/status")
				.header("X-ACCESS-TOKEN", testUserJwt)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		//then
		assertNotNull(postingRepository.findPostingByIdAndStatus(postCommentId.longValue(), DELETED));
	}

	@Test
	@DisplayName("37. 포스트 댓글 삭제")
	@Order(17)
	public void unAuthorizedDeletePostCommentTest() throws Exception {
		//given
		long postId = 1L;
		PostingCommentReq postingCommentReq = PostingCommentReq.builder()
			.content("포스트 테스트 댓글입니다")
			.build();

		//when
		MvcResult mvcResult = this.mockMvc.perform(post("/posts/" + postId + "/comments")
				.header("X-ACCESS-TOKEN", testUserJwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(postingCommentReq)))
			.andDo(print())
			.andExpect(jsonPath("$.code", 1000).exists())
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Number postCommentId = JsonPath.read(contentAsString, "$.result");

		// then
		// 포스트 댓글 삭제 시도
		assertThrows(NestedServletException.class,
			() -> this.mockMvc.perform(patch("/posts/" + postId + "/comments/" + postCommentId + "/status")
					.header("X-ACCESS-TOKEN", anotherUserJwt)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn());

	}

	@Test
	@DisplayName("51. 최근 포스트 게시글 Top 4")
	@Order(20)
	public void getTop4RecentPostingTest() {
		//given

		//when
		List<PostingRecentRes> top4RecentPosts = postingService.getTop4RecentPosts();

		//then
		assertEquals(top4RecentPosts.size(), 4);
		top4RecentPosts.forEach(postingRecentRes ->
			System.out.println("postingRecentRes.getTitle() = " + postingRecentRes.getTitle()));
	}

	@Test
	@DisplayName("52. 다른 유저가 작성한 포스트 리스트 조회")
	@Order(21)
	public void findMemberPostsTest() {
		//given
		Long memberId = 107L;

		//when
		List<PostingRes> memberPosts = postingService.findMemberPosts(memberId);

		//then
		assertEquals(memberPosts.size(), 3);
	}

}

