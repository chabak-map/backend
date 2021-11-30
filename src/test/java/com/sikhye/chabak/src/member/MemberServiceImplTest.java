package com.sikhye.chabak.src.member;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.member.entity.Member;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberServiceImplTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@AfterAll
	@Rollback(false)
	public void deleteMemberTable() {
		Optional<Member> member = memberRepository.findMemberByEmailAndStatus("mytest1234@mytest1234.com", BaseStatus.used);
		member.ifPresent(value -> memberRepository.delete(value));
	}

//	@Test
//	@DisplayName("001. 회원가입 테스트")
//	@Rollback(false)
//	@Order(1)
//	public void memberJoinTest() throws Exception {
//
//		//given
//		JoinReq joinReq = JoinReq.builder()
//			.email("spring123@naver.com")
//			.nickname("spring123")
//			.password("spring123")
//			.phoneNumber("01012341234")
//			.build();
//
//		//when
//		LoginRes result = memberService.join(joinReq);
//		Long memberId = result.getId();
//
//		Optional<Member> findMember = memberRepository.findMemberByEmailAndStatus("mytest1234@mytest1234.com", BaseStatus.used);
//		findMember.ifPresent(member -> assertEquals(memberId, member.getId()));
//
//	}
//
//
//	@Test
//	@DisplayName("002. 중복된 이메일 가입 시도")
//	@Order(2)
//	public void memberJoinDuplicatedEmailTest() throws BaseException {
//
//		//given
//		JoinReq joinReq = JoinReq.builder()
//			.email("spring123@naver.com")
//			.nickname("spr1234")
//			.password("spring123")
//			.phoneNumber("01012341234")
//			.build();
//
//		//when and then
//		BaseException exception = assertThrows(BaseException.class, () -> memberService.join(joinReq));
//		assertEquals(BaseResponseStatus.POST_USERS_EXISTS_EMAIL.getCode(), exception.getStatus().getCode());
//	}
//
//	@Test
//	@DisplayName("003. 중복된 닉네임 가입 시도")
//	@Order(3)
//	public void memberJoinDuplicatedNicknameTest() throws BaseException {
//
//		//given
//		JoinReq joinReq = JoinReq.builder()
//			.email("spr1234@naver.com")
//			.nickname("spring123")
//			.password("spring123")
//			.phoneNumber("01012341234")
//			.build();
//
//		//when and then
//		BaseException exception = assertThrows(BaseException.class, () -> memberService.join(joinReq));
//		assertEquals(BaseResponseStatus.POST_USERS_EXISTS_NICKNAME.getCode(), exception.getStatus().getCode());
//	}
//
//
//	@Test
//	@DisplayName("004. 로그인 테스트")
//	@Order(4)
//	public void memberLoginTest() throws BaseException {
//		//given
//		LoginReq loginReq = new LoginReq("spring123@naver.com", "spring123");
//
//		//when
//		LoginRes loginMember = memberService.login(loginReq);
//
//		//then
//		assertNotNull(loginMember);
//	}


//	@Test
//	@DisplayName("003. 멤버 조회 테스트")
//	public void memberLookupTest() throws Exception {
//
//		//given
//		Member memberByEmail = memberRepository.findMemberByEmail("test1234@test.com");
//
//		//when
//		Long memberId = memberByEmail.getId();
//		MemberDto memberDto = memberService.lookup();
//
//		//then
//		assertThat(memberDto.getNickname()).isEqualTo("test1234");
//
//	}

//	@Test
//	@Order(1)
//	@DisplayName("004. 멤버 휴대폰 인증")
//	public void requestPhoneAuthTest() throws Exception {
//		//given
////		String phoneNumber = "";
//		String phoneNumber = "01012341234";
//
//		//when
//		memberService.requestPhoneAuth(phoneNumber);
//
//
//	}
//
//	@Test
//	@Order(2)
//	@DisplayName("005. 휴대폰 인증")
//	public void verifySmsTest() throws Exception {
//	    //given
//	    String code = "111111";
//		String phone = "01012341234";
//
//
//	    //when
//		Boolean aBoolean = memberService.verifySms(code, phone);
//
//		//then
//	}

//	@Test
//	@Order(1)
//	@DisplayName("006. AES256 테스트")
//	public void aes256Test() throws Exception {
//		//given
//		String s = memberService.aes256Method();
//		Assertions.assertEquals(s, "test");
//
//		//when
//
//		//then
//	}


}
