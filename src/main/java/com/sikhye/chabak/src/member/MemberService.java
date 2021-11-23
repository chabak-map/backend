package com.sikhye.chabak.src.member;

import com.sikhye.chabak.base.exception.BaseException;
import com.sikhye.chabak.src.member.dto.JoinReq;
import com.sikhye.chabak.src.member.dto.LoginReq;
import com.sikhye.chabak.src.member.dto.LoginRes;
import com.sikhye.chabak.src.member.dto.MemberDto;
import org.springframework.web.multipart.MultipartFile;

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


}
