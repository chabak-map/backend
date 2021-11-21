package com.sikhye.chabak.src.member;

import com.sikhye.chabak.src.member.dto.JoinReq;
import com.sikhye.chabak.src.member.dto.LoginReq;
import com.sikhye.chabak.src.member.dto.LoginRes;
import com.sikhye.chabak.src.member.entity.Member;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

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
	public void deleteMemberTable() throws Exception {
		memberRepository.deleteAll();
	}

	@Test
	@DisplayName("001. 회원가입 테스트")
	@Rollback(false)
	@Order(1)
	public void memberJoinTest() throws Exception {

		//given
		JoinReq joinReq = JoinReq.builder()
			.email("test1234@test.com")
			.nickname("test1234")
			.password("test1234")
			.phoneNumber("01012341234")
			.build();

		//when
		LoginRes result = memberService.join(joinReq);
		Long memberId = result.getId();

		Member memberByEmail = memberRepository.findMemberByEmail("test1234@test.com");

		//then (회원가입 결과 ID == 찾아온 ID)
		assertThat(memberId).isEqualTo(memberByEmail.getId());
	}

	@Test
	@DisplayName("002. 로그인 테스트")
	@Order(2)
	public void memberLoginTest() throws Exception {
		//given
		LoginReq loginReq = new LoginReq("test1234@test.com", "test1234");

		//when	- 인증되지 않은 사용자는 로그인 실패
		LoginRes loginMember = memberService.login(loginReq);

		//then
		assertThat(loginMember).isNotNull();
	}


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


}
