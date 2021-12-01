package com.sikhye.chabak.src.post;

import com.sikhye.chabak.base.exception.BaseException;
import com.sikhye.chabak.src.aws.BasicUploadService;
import com.sikhye.chabak.src.comment.repository.PostingCommentRepository;
import com.sikhye.chabak.src.post.dto.PostingDetailRes;
import com.sikhye.chabak.src.post.dto.PostingReq;
import com.sikhye.chabak.src.post.dto.PostingRes;
import com.sikhye.chabak.src.post.entity.Posting;
import com.sikhye.chabak.src.post.entity.PostingImage;
import com.sikhye.chabak.src.post.repository.PostingImageRepository;
import com.sikhye.chabak.src.post.repository.PostingRepository;
import com.sikhye.chabak.utils.JwtService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.sikhye.chabak.base.BaseResponseStatus.NOT_TO_DELETE;
import static com.sikhye.chabak.base.BaseResponseStatus.SEARCH_NOT_FOUND_POST;
import static com.sikhye.chabak.base.entity.BaseStatus.used;

@Slf4j
@Service
@Transactional
public class PostingServiceImpl implements PostingService {

	private final PostingRepository postingRepository;
	private final PostingImageRepository postingImageRepository;
	private final PostingCommentRepository postingCommentRepository;
	private final BasicUploadService s3UploadService;
	private final JwtService jwtService;

	@Builder
	public PostingServiceImpl(PostingRepository postingRepository, PostingImageRepository postingImageRepository, PostingCommentRepository postingCommentRepository, BasicUploadService s3UploadService, JwtService jwtService) {
		this.postingRepository = postingRepository;
		this.postingImageRepository = postingImageRepository;
		this.postingCommentRepository = postingCommentRepository;
		this.s3UploadService = s3UploadService;
		this.jwtService = jwtService;
	}

	@Override
	public List<PostingRes> findPostsWithPaging() {
		return null;
	}

	@Override
	public List<PostingRes> findPosts() {
		List<Posting> findPostings = postingRepository.findPostingsByStatus(used).orElse(Collections.emptyList());

		return getPostingResList(findPostings);

	}

	@Override
	public List<PostingRes> findMemberPosts() {
		Long memberId = jwtService.getUserIdx();
		List<Posting> memberPostings = postingRepository.findPostingsByMemberIdAndStatus(memberId, used).orElse(Collections.emptyList());

		return getPostingResList(memberPostings);
	}


	@Override
	@Transactional
	public Long createPost(PostingReq postingReq) {
		Long memberId = jwtService.getUserIdx();

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
		Posting findPost = postingRepository.findPostingByIdAndStatus(postId, used).orElseThrow(() -> new BaseException(SEARCH_NOT_FOUND_POST));

		return PostingDetailRes.builder()
			.title(findPost.getTitle())
			.content(findPost.getContent())
			.nickname(findPost.getMember().getNickname())
			.profileImageUrl(findPost.getMember().getImageUrl())
			.postImageUrls(findPost.getPostingImages().stream().map(PostingImage::getImageUrl).collect(Collectors.toList()))
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
		Posting toDeletePost = postingRepository.findPostingByIdAndStatus(postId, used).orElseThrow(() -> new BaseException(NOT_TO_DELETE));
		toDeletePost.setStatusToDelete();

		return toDeletePost.getId();
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
//					String postingImageUrl = postingImageRepository.findTop1ByPostingIdAndStatus(posting.getId(), used).map(PostingImage::getImageUrl).orElse("");
//					Long commentCount = postingCommentRepository.countByPostingIdAndStatus(posting.getId(), used);

					return PostingRes.builder()
						.title(posting.getTitle())
						.nickname(posting.getMember().getNickname())
						.imageUrl(posting.getPostingImages().isEmpty() ? "" : posting.getPostingImages().get(0).getImageUrl())    // TODO: NPE 우려
						.commentCount((long) posting.getPostingComments().size())
						.build();
				}
			)
			.collect(Collectors.toList());
	}

}
