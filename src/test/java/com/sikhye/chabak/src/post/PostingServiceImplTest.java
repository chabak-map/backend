package com.sikhye.chabak.src.post;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

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
import com.sikhye.chabak.service.post.PostingService;
import com.sikhye.chabak.service.post.dto.PostingCommentRes;
import com.sikhye.chabak.service.post.dto.PostingDetailRes;
import com.sikhye.chabak.service.post.dto.PostingRes;
import com.sikhye.chabak.service.post.dto.PostingTagReq;
import com.sikhye.chabak.service.post.dto.PostingTagRes;
import com.sikhye.chabak.service.post.entity.PostingTag;
import com.sikhye.chabak.service.post.repository.PostingCommentRepository;
import com.sikhye.chabak.service.post.repository.PostingRepository;
import com.sikhye.chabak.service.post.repository.PostingTagRepository;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
	private EntityManager em;

	@Test
	@DisplayName("001. 포스팅 전체 조회 테스트")
	@Order(1)
	public void findPostingsTest() {
		//given
		List<PostingRes> posts = postingService.findPosts();

		//when

		//then
		assertEquals(posts.size(), 3);
	}

	//	@Test
	//	@DisplayName("002. 멤버가 작성한 포스팅 조회 테스트")
	//	@Order(2)
	//	public void findMyPostingsTest() {
	//		//given
	//		List<PostingRes> postingResList = postingService.forTestFindMemberPosts();
	//
	//		//when
	//
	//
	//		//then
	//		assertEquals(postingResList.size(), 3);
	//	}

	@Test
	@DisplayName("03. 포스트 상세 조회 테스트")
	@Order(3)
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

	@Test
	@DisplayName("05. 포스팅 태그 조회")
	@Order(5)
	public void findPostingTagsTest() {
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
	@DisplayName("06. 포스팅 태그 등록")
	@Order(6)
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
	@DisplayName("07. 포스팅 태그 수정")
	@Order(7)
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
	@DisplayName("08. 포스팅 태그 삭제")
	@Order(8)
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
	@DisplayName("09. 포스트 댓글 리스트")
	@Order(9)
	public void findPostCommentsTest() {
		//given
		List<PostingCommentRes> postComments = postingService.findPostComments(1L);

		//when

		//then
		assertEquals(postComments.size(), 1);
	}

	// JWT 토큰 필요
	// @Test
	// @DisplayName("10. 포스트 댓글 작성")
	// @Order(10)
	// public void addPostCommentTest() {
	// 	//given
	// 	Long commentId = postingService.addPostComment(1L, new PostingCommentReq("포스트 댓글 테스트"));
	//
	// 	//when
	// 	PostingComment postingComment = postingCommentRepository.findPostingCommentByIdAndStatus(commentId, USED);
	//
	// 	//then
	// 	assertEquals(postingComment.getContent(), "포스트 댓글 테스트");
	// }
	//
	// @Test
	// @DisplayName("11. 포스트 댓글 수정")
	// @Order(11)
	// public void editPostCommentTest() {
	// 	//given
	// 	Long commentId = postingService.addPostComment(1L, new PostingCommentReq("포스트 댓글 테스트"));
	// 	PostingComment postingComment = postingCommentRepository.findPostingCommentByIdAndStatus(commentId, USED);
	// 	Long editCommentId = postingService.editPostComment(1L, postingComment.getId(),
	// 		new PostingCommentReq("수정된 포스트 댓글 테스트"));
	//
	// 	em.flush();
	// 	em.clear();
	//
	// 	//when
	// 	PostingComment findPostingComment = postingCommentRepository.findPostingCommentByIdAndStatus(editCommentId,
	// 		USED);
	//
	// 	//then
	// 	assertEquals(findPostingComment.getContent(), "수정된 포스트 댓글 테스트");
	// }
	//
	// @Test
	// @DisplayName("12. 포스트 댓글 삭제")
	// @Order(12)
	// public void deletePostCommentTest() {
	// 	//given
	// 	Long deletedId = postingService.statusToDeletePostComment(1L, 1L);
	//
	// 	em.flush();
	// 	em.clear();
	//
	// 	//when
	// 	PostingComment deletedComment = postingCommentRepository.findById(deletedId)
	// 		.orElseThrow(() -> new BaseException(DELETE_EMPTY));
	//
	// 	//then
	// 	assertEquals(deletedComment.getStatus(), DELETED);
	// }

}

