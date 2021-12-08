package com.sikhye.chabak.src.member;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.member.MemberRepository;
import com.sikhye.chabak.service.member.MemberService;
import com.sikhye.chabak.service.member.dto.EditMemberReq;
import com.sikhye.chabak.service.member.dto.JoinReq;
import com.sikhye.chabak.service.member.dto.LoginReq;
import com.sikhye.chabak.service.member.dto.LoginRes;
import com.sikhye.chabak.service.member.dto.MemberDto;
import com.sikhye.chabak.service.member.entity.Member;

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
		Optional<Member> member = memberRepository.findMemberByEmailAndStatus("admintest@admintest.com",
			BaseStatus.USED);
		member.ifPresent(value -> memberRepository.delete(value));
	}

	@Test
	@DisplayName("001. 회원가입 테스트")
	@Rollback(false)
	@Order(1)
	public void memberJoinTest() {

		//given
		JoinReq joinReq = JoinReq.builder()
			.email("admintest@admintest.com")
			.nickname("admin123")
			.password("admin123")
			.phoneNumber("01012341234")
			.build();

		//when
		LoginRes result = memberService.join(joinReq);
		Long memberId = result.getId();

		Optional<Member> findMember = memberRepository.findMemberByEmailAndStatus("admintest@admintest.com",
			BaseStatus.USED);
		findMember.ifPresent(member -> assertEquals(memberId, member.getId()));

	}

	@Test
	@DisplayName("002. 중복된 이메일 가입 시도")
	@Order(2)
	public void memberJoinDuplicatedEmailTest() throws BaseException {

		//given
		JoinReq joinReq = JoinReq.builder()
			.email("admintest@admintest.com")
			.nickname("admin123")
			.password("admin123")
			.phoneNumber("01012341234")
			.build();

		//when and then
		BaseException exception = assertThrows(BaseException.class, () -> memberService.join(joinReq));
		assertEquals(POST_USERS_EXISTS_EMAIL.getCode(), exception.getStatus().getCode());
	}

	@Test
	@DisplayName("003. 중복된 닉네임 가입 시도")
	@Order(3)
	public void memberJoinDuplicatedNicknameTest() throws BaseException {

		//given
		JoinReq joinReq = JoinReq.builder()
			.email("admintest123@admintest.com")
			.nickname("admin123")
			.password("admin123")
			.phoneNumber("01012341234")
			.build();

		//when and then
		BaseException exception = assertThrows(BaseException.class, () -> memberService.join(joinReq));
		assertEquals(POST_USERS_EXISTS_NICKNAME.getCode(), exception.getStatus().getCode());
	}

	@Test
	@DisplayName("004. 로그인 테스트")
	@Order(4)
	public void memberLoginTest() throws BaseException {
		//given
		LoginReq loginReq = new LoginReq("admintest@admintest.com", "admin123");

		//when
		LoginRes loginMember = memberService.login(loginReq);

		//then
		assertNotNull(loginMember);
	}

	@Test
	@DisplayName("005. 멤버 조회 테스트")
	@Order(5)
	public void memberLookupTest() {

		//given
		Member memberByEmail = memberRepository
			.findMemberByEmailAndStatus("admintest@admintest.com", BaseStatus.USED)
			.orElseThrow(() -> new BaseException(CHECK_USER));

		//when
		Long memberId = memberByEmail.getId();
		MemberDto memberDto = memberService.lookup();

		//then
		assertEquals(memberDto.getNickname(), "admin123");

	}

	@Test
	@Order(6)
	@DisplayName("006. 멤버 휴대폰 인증")
	public void requestPhoneAuthTest() {
		//given
		String phoneNumber = "01012341234";

		//when
		memberService.requestPhoneAuth(phoneNumber);

	}

	@Test
	@Order(7)
	@DisplayName("007. 업로드 이미지 - 이미지가 없는 경우")
	public void uploadImageTest() {
		EditMemberReq editMemberReq = EditMemberReq.builder()
			.nickname("nickname")
			.build();

		memberService.editMemberInform(editMemberReq);

	}

}
