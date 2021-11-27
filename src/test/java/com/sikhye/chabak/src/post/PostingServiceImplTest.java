package com.sikhye.chabak.src.post;

import com.sikhye.chabak.src.post.dto.PostingDetailRes;
import com.sikhye.chabak.src.post.dto.PostingRes;
import com.sikhye.chabak.src.post.repository.PostingRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

	@Test
	@DisplayName("001. 포스팅 전체 조회 테스트")
	@Order(1)
	public void findPostingsTest() {
		//given
		List<PostingRes> posts = postingService.findPosts();

		//when

		//then
		assertEquals(posts.size(), 5);
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
	@DisplayName("003. 포스트 상세 조회 테스트")
	@Order(3)
	public void findPostDetailTest() {
		//given
		PostingDetailRes postDetail = postingService.findPostDetail(5L);

		//when
		List<String> postImageUrls = postDetail.getPostImageUrls();

		long count = postImageUrls.stream()
			.filter(s -> s.equals("https://univ-city-bucket.s3.ap-northeast-2.amazonaws.com/images/posting/5/5-1.png"))
			.count();


		//then
		assertEquals(postDetail.getNickname(), "mynick");
		assertEquals(count, 1L);


	}


}
