package com.sikhye.chabak.src.comment;

import com.sikhye.chabak.base.exception.BaseException;
import com.sikhye.chabak.src.comment.dto.CommentReq;
import com.sikhye.chabak.src.comment.dto.CommentRes;
import com.sikhye.chabak.src.comment.entity.PlaceReview;
import com.sikhye.chabak.src.comment.entity.PostingComment;
import com.sikhye.chabak.src.comment.repository.PlaceReviewRepository;
import com.sikhye.chabak.src.comment.repository.PostingCommentRepository;
import com.sikhye.chabak.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.sikhye.chabak.base.BaseResponseStatus.DELETE_EMPTY;
import static com.sikhye.chabak.base.BaseResponseStatus.INVALID_USER_JWT;
import static com.sikhye.chabak.base.entity.BaseStatus.used;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

	private final PlaceReviewRepository placeReviewRepository;
	private final PostingCommentRepository postingCommentRepository;
	private final JwtService jwtService;


	@Override
	public List<CommentRes> findPlaceComments(Long placeId) {
		List<PlaceReview> placeReviews = placeReviewRepository.findPlaceReviewsByPlaceIdAndStatus(placeId, used).orElse(Collections.emptyList());

		return placeReviews.stream()
			.map(placeReview -> CommentRes.builder()
				.name(placeReview.getMember().getNickname())
				.content(placeReview.getContent())
				.writingDate(placeReview.getCreatedAt().toLocalDate())
				.build()).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Long addPlaceComment(Long placeId, CommentReq commentReq) {
		Long memberId = jwtService.getUserIdx();

		PlaceReview toSavePlaceReview = PlaceReview.builder()
			.placeId(placeId)
			.memberId(memberId)
			.content(commentReq.getContent())
			.build();

		return placeReviewRepository.save(toSavePlaceReview).getId();

	}

	@Override
	@Transactional
	public Long editPlaceComment(Long placeId, Long commentId, CommentReq commentReq) {
		Long memberId = jwtService.getUserIdx();

		PlaceReview findPlaceReview = placeReviewRepository.findPlaceReviewByIdAndStatus(commentId, used);

		if (!memberId.equals(findPlaceReview.getMemberId())) {
			throw new BaseException(INVALID_USER_JWT);
		}

		findPlaceReview.setContent(commentReq.getContent());

		return findPlaceReview.getId();
	}

	@Override
	@Transactional
	public Long statusToDeletePlaceComment(Long placeId, Long commentId) {
		Long memberId = jwtService.getUserIdx();

		PlaceReview findPlaceReview = placeReviewRepository.findPlaceReviewByIdAndStatus(commentId, used);

		if (!memberId.equals(findPlaceReview.getMemberId())) {
			throw new BaseException(INVALID_USER_JWT);
		} else if (!placeId.equals(findPlaceReview.getPlaceId())) {
			throw new BaseException(DELETE_EMPTY);
		} else {
			findPlaceReview.setStatusToDelete();

			return findPlaceReview.getId();
		}
	}

	@Override
	public List<CommentRes> findPostComments(Long postId) {
		List<PostingComment> postingComments = postingCommentRepository
			.findPostingCommentsByPostingIdAndStatus(postId, used).orElse(Collections.emptyList());

		return postingComments.stream()
			.map(postingComment -> CommentRes.builder()
				.name(postingComment.getMember().getNickname())
				.content(postingComment.getContent())
				.writingDate(postingComment.getCreatedAt().toLocalDate())
				.build()).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Long addPostComment(Long postId, CommentReq commentReq) {
		Long memberId = jwtService.getUserIdx();

		PostingComment toSavePostingComment = PostingComment.builder()
			.postingId(postId)
			.memberId(memberId)
			.content(commentReq.getContent())
			.build();

		return postingCommentRepository.save(toSavePostingComment).getId();

	}

	@Override
	@Transactional
	public Long editPostComment(Long postId, Long commentId, CommentReq commentReq) {
		Long memberId = jwtService.getUserIdx();

		PostingComment findPostingComment = postingCommentRepository.findPostingCommentByIdAndStatus(commentId, used);

		if (!memberId.equals(findPostingComment.getMemberId())) {
			throw new BaseException(INVALID_USER_JWT);
		}

		findPostingComment.setContent(commentReq.getContent());

		return findPostingComment.getId();
	}

	@Override
	@Transactional
	public Long statusToDeletePostComment(Long postId, Long commentId) {
		Long memberId = jwtService.getUserIdx();

		PostingComment findPostingComment = postingCommentRepository.findPostingCommentByIdAndStatus(commentId, used);

		if (!memberId.equals(findPostingComment.getMemberId())) {
			throw new BaseException(INVALID_USER_JWT);
		} else if (!postId.equals(findPostingComment.getPostingId())) {
			throw new BaseException(DELETE_EMPTY);
		} else {
			findPostingComment.setStatusToDelete();

			return findPostingComment.getId();
		}

	}
}
