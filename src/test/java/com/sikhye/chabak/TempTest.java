//package com.sikhye.chabak;
//
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@WebAppConfiguration
////@AutoConfigureMockMvc	// 컨트롤러 테스트 ( postman 쓸 것임 )
//// ptpt: (https://velog.io/@joosing/JUnit-BeforeAll-non-static%EC%9C%BC%EB%A1%9C-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Transactional
//class TempTest {
//
//	@Autowired
//	private MemberService memberService;
//
//	@Autowired
//	private MemberRepository memberRepository;
//
//	@PersistenceContext
//	private EntityManager em;
//
//// ptpt. 테스트 관련 어노테이션
////	@BeforeEach
////	@AfterEach
////	@Commit // rollback 하지 않음
////	@Rollback(false)
////	@DisplayName
//
//	@AfterAll
//	@Rollback(false)
//	public void deleteMemberTable() throws Exception {
//		memberRepository.deleteAll();
//	}
//
//	@Test
//	@DisplayName("001. 회원가입 테스트")
//	@Rollback(false)
//	public void memberJoinTest() throws Exception {
//
//
//
//		//when
//		PostMemberRes result = memberService.join(postMemberReq);
//		Long memberId = result.getMemberId();
//
//		Member memberByEmail = memberRepository.findMemberByEmail("phj0860@gmail.com");
//
//		//then
//		assertThat(memberId).isEqualTo(memberByEmail.getId());
//	}
//
//	@Test
//	@DisplayName("002. 로그인 테스트")
//	public void memberLoginTest() throws Exception {
//		//given
//		PostLoginReq postLoginReq = new PostLoginReq("phj0860@gmail.com", "test123");
//
//		//when	- 인증되지 않은 사용자는 로그인 실패
//		Assertions.assertThrows(BaseException.class, () -> memberService.login(postLoginReq));
////		PostMemberRes loginMember = memberService.login(postLoginReq);
//
//		//then
////		assertThat(loginMember).isNotNull();
//	}
//
////	@Test
////	@DisplayName("회원 정보 수정")
////	public void memberEditTest() throws Exception {
////		// given
////
////	}
//
//
//
//}
