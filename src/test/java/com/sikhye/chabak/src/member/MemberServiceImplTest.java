package com.sikhye.chabak.src.member;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.member.MemberRepository;
import com.sikhye.chabak.service.member.MemberService;
import com.sikhye.chabak.service.member.dto.EditMemberReq;
import com.sikhye.chabak.service.member.dto.JoinReq;
import com.sikhye.chabak.service.member.dto.LoginReq;
import com.sikhye.chabak.service.member.dto.LoginRes;
import com.sikhye.chabak.service.member.dto.PasswordReq;
import com.sikhye.chabak.service.member.entity.Member;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class MemberServiceImplTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private EntityManager em;

	ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
			.alwaysDo(print())
			.build();
	}

	@AfterAll
	@Rollback(false)
	public void deleteMemberTable() {
		Optional<Member> member = memberRepository.findMemberByEmailAndStatus("junit123@junit123.com",
			BaseStatus.USED);
		member.ifPresent(value -> memberRepository.delete(value));
	}

	@Test
	@DisplayName("01. 회원가입 테스트")
	@Rollback(false)
	@Order(1)
	public void memberJoinTest() {

		//given
		JoinReq joinReq = JoinReq.builder()
			.email("junit123@junit123.com")
			.nickname("junit123")
			.password("junit123")
			.phoneNumber("01099999999")
			.build();

		//when
		LoginRes result = memberService.join(joinReq);
		Long memberId = result.getId();

		Optional<Member> findMember = memberRepository.findMemberByEmailAndStatus("junit123@junit123.com",
			BaseStatus.USED);
		findMember.ifPresent(member -> assertEquals(memberId, member.getId()));

	}

	@Test
	@DisplayName("01-1. 중복된 이메일 가입 시도")
	@Order(2)
	public void memberJoinDuplicatedEmailTest() throws BaseException {

		//given
		JoinReq joinReq = JoinReq.builder()
			.email("junit123@junit123.com")
			.nickname("junit1234")
			.password("junit123")
			.phoneNumber("01099998888")
			.build();

		//when and then
		BaseException exception = assertThrows(BaseException.class, () -> memberService.join(joinReq));
		assertEquals(POST_USERS_EXISTS_EMAIL.getCode(), exception.getStatus().getCode());
	}

	@Test
	@DisplayName("01-2. 중복된 닉네임 가입 시도")
	@Order(3)
	public void memberJoinDuplicatedNicknameTest() throws BaseException {

		//given
		JoinReq joinReq = JoinReq.builder()
			.email("junit1234@junit123.com")
			.nickname("junit123")
			.password("junit123")
			.phoneNumber("01088889999")
			.build();

		//when and then
		BaseException exception = assertThrows(BaseException.class, () -> memberService.join(joinReq));
		assertEquals(POST_USERS_EXISTS_NICKNAME.getCode(), exception.getStatus().getCode());
	}

	@Test
	@DisplayName("02. 로그인 테스트")
	@Order(4)
	public void memberLoginTest() throws BaseException {
		//given
		LoginReq loginReq = new LoginReq("junit123@junit123.com", "junit123");

		//when
		LoginRes loginMember = memberService.login(loginReq);

		//then
		assertNotNull(loginMember);
	}

	@Test
	@DisplayName("03. 회원정보 조회 테스트")
	@Order(5)
	public void memberLookupTest() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/members")
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTAxLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MDU2ODE1LCJleHAiOjE2Mzk0MDI0MTV9.nV9Miz_0yYeJEI-ZTWHijp-sXQwFGVpV2K2LPQbx3MA"))

			.andDo(print())
			.andExpect(jsonPath("$.result.id", 50).exists())
			.andExpect(jsonPath("$.result.nickname", "junit123").exists())
			.andReturn();

		System.out.println("mvcResult = " + mvcResult.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("01-3. 중복된 휴대폰번호 가입 시도")
	@Order(6)
	public void memberJoinDuplicatedPhoneNumberTest() throws BaseException {

		//given
		JoinReq joinReq = JoinReq.builder()
			.email("junit1234@junit123.com")
			.nickname("junit1234")
			.password("junit123")
			.phoneNumber("01099999999")
			.build();

		//when and then
		BaseException exception = assertThrows(BaseException.class, () -> memberService.join(joinReq));
		assertEquals(POST_USERS_EXISTS_PHONE_NUMBER.getCode(), exception.getStatus().getCode());
	}

	@Test
	@Order(7)
	@DisplayName("36. 회원정보 수정")
	public void uploadImageTest() throws Exception {

		// Object To Json
		EditMemberReq editMemberReq = EditMemberReq.builder()
			.nickname("test5678")
			.build();

		MvcResult mvcResult = this.mockMvc.perform(patch("/members")
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTAxLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MDU2ODE1LCJleHAiOjE2Mzk0MDI0MTV9.nV9Miz_0yYeJEI-ZTWHijp-sXQwFGVpV2K2LPQbx3MA")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(editMemberReq)))
			.andDo(print())
			.andExpect(jsonPath("$.result", 101).exists())
			.andReturn();

		System.out.println(
			"mvcResult.getResponse().getContentAsString() = " + mvcResult.getResponse().getContentAsString());
	}

	@Test
	@Order(11)
	@DisplayName("37. 회원 비밀번호 수정")
	public void editPasswordTest() {

		//given
		PasswordReq passwordReq = PasswordReq.builder()
			.memberId(101L)
			.password("test5678")
			.build();

		//when
		Long memberId = memberService.editPassword(passwordReq);

		em.flush();
		em.clear();

		LoginReq loginReq = new LoginReq("test1234@test1234.com", "test5678");
		Long loginMemberId = memberService.login(loginReq).getId();

		//then
		assertEquals(memberId, loginMemberId);
	}

	@Test
	@Order(14)
	@DisplayName("38. 회원 이메일찾기")
	public void findMemberEmail() {

		//given
		PasswordReq passwordReq = PasswordReq.builder()
			.memberId(101L)
			.password("test1234")
			.build();

		//when
		Long memberId = memberService.editPassword(passwordReq);
		LoginReq loginReq = new LoginReq("test1234@test1234.com", "test1234");
		Long loginMemberId = memberService.login(loginReq).getId();

		//then
		assertEquals(memberId, loginMemberId);
	}

	@Test
	@Order(16)
	@DisplayName("39. 회원 비밀번호 찾기 검증")
	public void findMember() {

		//given
		String phoneNumber = "01012123434";
		String email = "test1234@test1234.com";

		//when
		Long findMemberId = memberService.findMember(phoneNumber, email);

		//then
		assertEquals(101L, findMemberId);
	}

	@Test
	@Order(18)
	@DisplayName("40. 회원 탈퇴")
	public void statusToDeleteMemberTest() throws Exception {

		//given
		String phoneNumber = "01012123434";
		String email = "test1234@test1234.com";

		MvcResult mvcResult = this.mockMvc.perform(patch("/members/status")
				.header("X-ACCESS-TOKEN",
					"eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJtZW1iZXJJZCI6MTAxLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM5MDU2ODE1LCJleHAiOjE2Mzk0MDI0MTV9.nV9Miz_0yYeJEI-ZTWHijp-sXQwFGVpV2K2LPQbx3MA")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(jsonPath("$.result", 101).exists())
			.andReturn();

		//then
		assertThrows(BaseException.class, () -> memberService.findMember(phoneNumber, email));
	}

}
