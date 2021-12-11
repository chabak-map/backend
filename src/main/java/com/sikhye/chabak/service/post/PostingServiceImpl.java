package com.sikhye.chabak.service.post;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static com.sikhye.chabak.global.response.BaseResponseStatus.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.image.UploadService;
import com.sikhye.chabak.service.jwt.JwtTokenService;
import com.sikhye.chabak.service.post.dto.PostingCommentReq;
import com.sikhye.chabak.service.post.dto.PostingCommentRes;
import com.sikhye.chabak.service.post.dto.PostingDetailRes;
import com.sikhye.chabak.service.post.dto.PostingReq;
import com.sikhye.chabak.service.post.dto.PostingRes;
import com.sikhye.chabak.service.post.dto.PostingTagReq;
import com.sikhye.chabak.service.post.dto.PostingTagRes;
import com.sikhye.chabak.service.post.entity.Posting;
import com.sikhye.chabak.service.post.entity.PostingComment;
import com.sikhye.chabak.service.post.entity.PostingImage;
import com.sikhye.chabak.service.post.entity.PostingTag;
import com.sikhye.chabak.service.post.repository.PostingCommentRepository;
import com.sikhye.chabak.service.post.repository.PostingImageRepository;
import com.sikhye.chabak.service.post.repository.PostingRepository;
import com.sikhye.chabak.service.post.repository.PostingTagRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PostingServiceImpl implements PostingService {

	private final PostingRepository postingRepository;
	private final PostingImageRepository postingImageRepository;
	private final PostingCommentRepository postingCommentRepository;
	private final PostingTagRepository postingTagRepository;
	private final UploadService s3UploadService;
	private final JwtTokenService jwtTokenService;

	public PostingServiceImpl(PostingRepository postingRepository,
		PostingImageRepository postingImageRepository,
		PostingCommentRepository postingCommentRepository,
		PostingTagRepository postingTagRepository, UploadService s3UploadService,
		JwtTokenService jwtTokenService) {
		this.postingRepository = postingRepository;
		this.postingImageRepository = postingImageRepository;
		this.postingCommentRepository = postingCommentRepository;
		this.postingTagRepository = postingTagRepository;
		this.s3UploadService = s3UploadService;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public List<PostingRes> findPostsWithPaging() {
		return null;
	}

	@Override
	public List<PostingRes> findPosts() {
		List<Posting> findPostings = postingRepository.findPostingsByStatus(USED).orElse(Collections.emptyList());

		return getPostingResList(findPostings);

	}

	@Override
	public List<PostingRes> findMemberPosts() {
		Long memberId = jwtTokenService.getMemberId();
		List<Posting> memberPostings = postingRepository.findPostingsByMemberIdAndStatus(memberId, USED)
			.orElse(Collections.emptyList());

		return getPostingResList(memberPostings);
	}

	@Override
	@Transactional
	public Long createPost(PostingReq postingReq) {
		Long memberId = jwtTokenService.getMemberId();

		// 글 처리
		Posting toSavePosting = Posting.builder()
			.title(postingReq.getTitle())
			.content(postingReq.getContent())
			.memberId(memberId)
			.build();

		Posting savedPosting = postingRepository.save(toSavePosting);

		if (postingReq.isEmptyOrNullImages()) {
			return toSavePosting.getId();
		}

		// 이미지 처리
		List<String> imageUrls = s3UploadService.uploadImages(postingReq.getImages(), "images/posting/");

		imageUrls.forEach(s ->
			postingImageRepository.save(PostingImage.builder()
				.imageUrl(s)
				.postingId(savedPosting.getId())
				.build()));

		return savedPosting.getId();
	}

	@Override
	public PostingDetailRes findPostDetail(Long postId) {
		Posting findPost = postingRepository.findPostingByIdAndStatus(postId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_POST));

		return PostingDetailRes.builder()
			.title(findPost.getTitle())
			.content(findPost.getContent())
			.nickname(findPost.getMember().getNickname())
			.profileImageUrl(findPost.getMember().getImageUrl())
			.postImageUrls(
				findPost.getPostingImages().stream().map(PostingImage::getImageUrl).collect(Collectors.toList()))
			.build();

	}

	@Override
	@Transactional
	public Long editPost(PostingReq postingReq, Long postId) {
		return null;
	}

	@Override
	@Transactional
	public Long statusToDeletePost(Long postId) {
		Posting toDeletePost = postingRepository.findPostingByIdAndStatus(postId, USED)
			.orElseThrow(() -> new BaseException(NOT_TO_DELETE));
		toDeletePost.setStatusToDelete();

		return toDeletePost.getId();
	}

	@Override
	public List<String> findPostingTags(Long postingId) {
		List<PostingTag> postingTags = postingTagRepository.findPostingTagsByPostingIdAndStatus(postingId, USED)
			.orElse(Collections.emptyList());

		return postingTags.stream()
			.map(PostingTag::getName)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<PostingTagRes> addPostingTags(Long postingId, PostingTagReq postingTagReq) {
		// 포스팅 작성 유저와 동일 권한인지 확인
		validateMember(postingId);

		List<String> postingTagNames = postingTagReq.getPostingTags();

		return postingTagNames.stream()
			.map(s -> {
				PostingTag toSavePostingTag = PostingTag.builder()
					.name(s)
					.postingId(postingId)
					.build();

				PostingTag savedPostingTag = postingTagRepository.save(toSavePostingTag);
				return new PostingTagRes(savedPostingTag.getId(), savedPostingTag.getName());
			})
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Long editPostingTag(Long postingId, Long postingTagId, String postingTagName) {
		validateMember(postingId);

		PostingTag findPostingTag = postingTagRepository.findPostingTagByIdAndStatus(postingTagId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_POST));

		if (!findPostingTag.getPostingId().equals(postingId))
			throw new BaseException(SEARCH_NOT_FOUND_POST);

		findPostingTag.setName(postingTagName);

		return postingTagId;
	}

	@Override
	@Transactional
	public Long postingTagStatusToDelete(Long postingId, Long postingTagId) {
		validateMember(postingId);

		PostingTag findPostingTag = postingTagRepository.findPostingTagByIdAndStatus(postingTagId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_POST));

		if (!findPostingTag.getPostingId().equals(postingId))
			throw new BaseException(SEARCH_NOT_FOUND_POST);

		findPostingTag.setStatusToDelete();

		return postingTagId;
	}

	// TODO: 글에서 이미지만 관리하는 API 필요 ( 이미지 CRUD )

	// ==================================
	// INTERNAL USE
	// ==================================
	@NotNull
	private List<PostingRes> getPostingResList(List<Posting> postings) {
		return postings.stream()
			.map(posting -> {
					// ptpt: Optional map 사용
					//               String postingImageUrl = postingImageRepository.findTop1ByPostingIdAndStatus(posting.getId(), used).map(PostingImage::getImageUrl).orElse("");
					//               Long commentCount = postingCommentRepository.countByPostingIdAndStatus(posting.getId(), used);

					return PostingRes.builder()
						.id(posting.getId())
						.title(posting.getTitle())
						.nickname(posting.getMember().getNickname())
						.imageUrl(posting.getPostingImages().isEmpty() ? "" :
							posting.getPostingImages().get(0).getImageUrl())    // TODO: NPE 우려
						.commentCount((long)posting.getPostingComments().size())
						.build();
				}
			)
			.collect(Collectors.toList());
	}

	@Override
	public List<PostingCommentRes> findPostComments(Long postId) {
		List<PostingComment> postingComments = postingCommentRepository
			.findPostingCommentsByPostingIdAndStatus(postId, USED).orElse(Collections.emptyList());

		return postingComments.stream()
			.map(postingComment -> PostingCommentRes.builder()
				.name(postingComment.getMember().getNickname())
				.content(postingComment.getContent())
				.writingDate(postingComment.getCreatedAt().toLocalDate())
				.build()).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Long addPostComment(Long postId, PostingCommentReq commentReq) {
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
	public Long editPostComment(Long postId, Long commentId, PostingCommentReq commentReq) {
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

	// ==============================================
	// INTERNAL USE
	// ==============================================
	private void validateMember(Long postingId) {
		Long memberId = jwtTokenService.getMemberId();
		Long findMemberId = postingRepository.findPostingByIdAndStatus(postingId, USED)
			.orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_POST)).getMemberId();

		if (!memberId.equals(findMemberId)) {
			throw new BaseException(INVALID_USER_JWT);
		}
	}

}
