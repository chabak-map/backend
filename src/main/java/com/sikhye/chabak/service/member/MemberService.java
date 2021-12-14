package com.sikhye.chabak.service.member;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.member.dto.EditMemberReq;
import com.sikhye.chabak.service.member.dto.JoinReq;
import com.sikhye.chabak.service.member.dto.LoginReq;
import com.sikhye.chabak.service.member.dto.LoginRes;
import com.sikhye.chabak.service.member.dto.MemberDto;
import com.sikhye.chabak.service.member.dto.PasswordReq;
import com.sikhye.chabak.service.member.entity.Member;
import com.sikhye.chabak.service.oauth.constant.OAuthType;

public interface MemberService {

	// 01. 로그인
	LoginRes login(LoginReq loginReq);

	// 02. 회원가입
	LoginRes join(JoinReq joinReq);

	// 03. 회원정보 조회
	MemberDto lookup();

	// 04. 휴대폰 인증 요청
	String requestPhoneAuth(String phoneNumber) throws BaseException;

	// 05. 휴대폰 인증 요청 확인
	Boolean verifySms(String verifyCode, String phoneNumber) throws BaseException;

	// 06. 회원 프로필 이미지 업로드
	String uploadImage(MultipartFile memberImage);

	// 07. 회원 정보 수정
	Long editMemberInform(EditMemberReq editMemberReq);

	// 08. 패스워드 수정
	Long editPassword(PasswordReq passwordReq);

	// 09. 회원 이메일 찾기
	String findMemberEmail(String phoneNumber);

	// 09-1. 회원 비밀번호 찾기 (이메일, 휴대폰번호)
	Long findMember(String phoneNumber, String email);

	// 10. 회원 탈퇴
	Long statusToDeleteMember();

	// 11. 회원 닉네임 중복체크
	Boolean isDuplicatedNickname(String nickname);

	// 12. 회원 이메일 중복체크
	Boolean isDuplicatedEmail(String email);

	// 13. 이메일로 회원찾기
	Optional<Member> findMemberBy(String email);

	Optional<Member> findMemberBy(OAuthType socialType, String socialId);

	Optional<Member> findMemberBy(Long memberId);

}
