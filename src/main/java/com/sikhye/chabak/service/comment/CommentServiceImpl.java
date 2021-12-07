package com.sikhye.chabak.service.comment;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.comment.dto.CommentReq;
import com.sikhye.chabak.service.comment.dto.CommentRes;
import com.sikhye.chabak.service.comment.entity.PlaceReview;
import com.sikhye.chabak.service.comment.entity.PostingComment;
import com.sikhye.chabak.service.comment.repository.PlaceReviewRepository;
import com.sikhye.chabak.service.comment.repository.PostingCommentRepository;
import com.sikhye.chabak.service.jwt.JwtTokenService;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

	private final PlaceReviewRepository placeReviewRepository;
	private final PostingCommentRepository postingCommentRepository;
	private final JwtTokenService jwtTokenService;

	@Builder
	public CommentServiceImpl(PlaceReviewRepository placeReviewRepository,
		PostingCommentRepository postingCommentRepository, JwtTokenService jwtTokenService) {
		this.placeReviewRepository = placeReviewRepository;
		this.postingCommentRepository = postingCommentRepository;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public List<CommentRes> findPlaceComments(Long placeId) {
		List<PlaceReview> placeReviews = placeReviewRepository.findPlaceReviewsByPlaceIdAndStatus(placeId, USED)
			.orElse(Collections.emptyList());

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
		Long memberId = jwtTokenService.getMemberId();

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
		Long memberId = jwtTokenService.getMemberId();

		PlaceReview findPlaceReview = placeReviewRepository.findPlaceReviewByIdAndStatus(commentId, USED);

		if (!memberId.equals(findPlaceReview.getMemberId())) {
			throw new BaseException(INVALID_USER_JWT);
		}

		findPlaceReview.setContent(commentReq.getContent());

		return findPlaceReview.getId();
	}

	@Override
	@Transactional
	public Long statusToDeletePlaceComment(Long placeId, Long commentId) {
		Long memberId = jwtTokenService.getMemberId();

		PlaceReview findPlaceReview = placeReviewRepository.findPlaceReviewByIdAndStatus(commentId, USED);

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
			.findPostingCommentsByPostingIdAndStatus(postId, USED).orElse(Collections.emptyList());

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
		Long memberId = jwtTokenService.getMemberId();

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
		Long memberId = jwtTokenService.getMemberId();

		PostingComment findPostingComment = postingCommentRepository.findPostingCommentByIdAndStatus(commentId, USED);

		if (!memberId.equals(findPostingComment.getMemberId())) {
			throw new BaseException(INVALID_USER_JWT);
		}

		findPostingComment.setContent(commentReq.getContent());

		return findPostingComment.getId();
	}

	@Override
	@Transactional
	public Long statusToDeletePostComment(Long postId, Long commentId) {
		Long memberId = jwtTokenService.getMemberId();

		PostingComment findPostingComment = postingCommentRepository.findPostingCommentByIdAndStatus(commentId, USED);

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
