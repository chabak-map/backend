package com.sikhye.chabak.src.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sikhye.chabak.base.BaseException;
import com.sikhye.chabak.src.member.dto.JoinReq;
import com.sikhye.chabak.src.member.dto.LoginReq;
import com.sikhye.chabak.src.member.dto.LoginRes;
import com.sikhye.chabak.src.member.dto.MemberDto;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public interface MemberService {

	// 01. 로그인
	LoginRes login(LoginReq loginReq) throws BaseException;

	// 02. 회원가입
	LoginRes join(JoinReq joinReq) throws BaseException;

	// 03. 회원정보 조회
	MemberDto lookup() throws BaseException;

	// 04. 휴대폰 인증 요청
	String requestPhoneAuth(String phoneNumber) throws BaseException;

	// 05. 휴대폰 인증 요청 확인
	Boolean verifySms(String verifyCode, String phoneNumber) throws BaseException;

}
