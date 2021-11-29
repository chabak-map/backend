package com.sikhye.chabak.src.comment;

import com.sikhye.chabak.base.exception.BaseException;
import com.sikhye.chabak.src.comment.dto.CommentReq;
import com.sikhye.chabak.src.comment.dto.CommentRes;
import com.sikhye.chabak.src.comment.entity.PlaceReview;
import com.sikhye.chabak.src.comment.entity.PostingComment;
import com.sikhye.chabak.src.comment.repository.PlaceReviewRepository;
import com.sikhye.chabak.src.comment.repository.PostingCommentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sikhye.chabak.base.BaseResponseStatus.DELETE_EMPTY;
import static com.sikhye.chabak.base.entity.BaseStatus.deleted;
import static com.sikhye.chabak.base.entity.BaseStatus.used;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceImplTest {

	@Autowired
	private CommentService commentService;

	@Autowired
	private PlaceReviewRepository placeReviewRepository;

	@Autowired
	private PostingCommentRepository postingCommentRepository;

	@Autowired
	private EntityManager em;


	@Test
	@DisplayName("001. 장소 댓글 조회")
	@Order(1)
	public void findPlaceCommentsTest() throws Exception {
		//given
		List<CommentRes> placeComments = commentService.findPlaceComments(1L);

		//when


		//then
		assertEquals(placeComments.size(), 2);

	}

	@Test
	@DisplayName("002. 장소 댓글 작성")
	@Order(2)
	public void addPlaceCommentTest() throws Exception {
		//given
		Long commentId = commentService.addPlaceComment(1L, new CommentReq("장소 댓글 테스트"));


		//when
		PlaceReview placeReview = placeReviewRepository.findPlaceReviewByIdAndStatus(commentId, used);


		//then
		assertEquals(placeReview.getContent(), "장소 댓글 테스트");
	}

	@Test
	@DisplayName("003. 장소 댓글 수정")
	@Order(3)
	public void editPlaceCommentTest() throws Exception {
		//given
		Long commentId = commentService.addPlaceComment(1L, new CommentReq("장소 댓글 테스트"));
		PlaceReview placeReview = placeReviewRepository.findPlaceReviewByIdAndStatus(commentId, used);
		Long editCommentId = commentService.editPlaceComment(1L, placeReview.getId(), new CommentReq("수정된 장소 댓글 테스트"));

		em.flush();
		em.clear();

		//when
		PlaceReview findPlaceReview = placeReviewRepository.findPlaceReviewByIdAndStatus(editCommentId, used);

		//then
		assertEquals(findPlaceReview.getContent(), "수정된 장소 댓글 테스트");
	}

	@Test
	@DisplayName("004. 장소 댓글 삭제")
	@Order(4)
	public void deletePlaceCommentTest() throws Exception {
		//given
		Long deletedId = commentService.statusToDeletePlaceComment(1L, 1L);

		em.flush();
		em.clear();

		//when
		PlaceReview deletedReview = placeReviewRepository.findById(deletedId)
			.orElseThrow(() -> new BaseException(DELETE_EMPTY));

		//then
		assertEquals(deletedReview.getStatus(), deleted);

	}

	@Test
	@DisplayName("005. 포스트 댓글 리스트")
	@Order(5)
	public void findPostCommentsTest() throws Exception {
		//given
		List<CommentRes> postComments = commentService.findPostComments(1L);

		//when

		//then
		assertEquals(postComments.size(), 2);
	}

	@Test
	@DisplayName("006. 포스트 댓글 작성")
	@Order(6)
	public void addPostCommentTest() throws Exception {
		//given
		Long commentId = commentService.addPostComment(1L, new CommentReq("포스트 댓글 테스트"));


		//when
		PostingComment postingComment = postingCommentRepository.findPostingCommentByIdAndStatus(commentId, used);


		//then
		assertEquals(postingComment.getContent(), "장소 댓글 테스트");
	}

	@Test
	@DisplayName("007. 포스트 댓글 수정")
	@Order(7)
	public void editPostCommentTest() throws Exception {
		//given
		Long commentId = commentService.addPostComment(1L, new CommentReq("포스트 댓글 테스트"));
		PostingComment postingComment = postingCommentRepository.findPostingCommentByIdAndStatus(commentId, used);
		Long editCommentId = commentService.editPostComment(1L, postingComment.getId(),
			new CommentReq("수정된 포스트 댓글 테스트"));

		em.flush();
		em.clear();

		//when
		PostingComment findPostingComment = postingCommentRepository.findPostingCommentByIdAndStatus(editCommentId, used);

		//then
		assertEquals(findPostingComment.getContent(), "수정된 포스트 댓글 테스트");
	}

	@Test
	@DisplayName("008. 포스트 댓글 삭제")
	@Order(8)
	public void deletePostCommentTest() throws Exception {
		//given
		Long deletedId = commentService.statusToDeletePostComment(1L, 3L);

		em.flush();
		em.clear();

		//when
		PostingComment deletedComment = postingCommentRepository.findById(deletedId)
			.orElseThrow(() -> new BaseException(DELETE_EMPTY));

		//then
		assertEquals(deletedComment.getStatus(), deleted);
	}


}
