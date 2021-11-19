package com.sikhye.chabak.src.member;

import com.sikhye.chabak.base.BaseException;
import com.sikhye.chabak.src.member.dto.JoinReq;
import com.sikhye.chabak.src.member.dto.LoginReq;
import com.sikhye.chabak.src.member.dto.LoginRes;
import com.sikhye.chabak.src.member.dto.MemberDto;

public interface MemberService {

	// 01. 로그인
	LoginRes login(LoginReq loginReq) throws BaseException;

	// 02. 회원가입
	Long join(JoinReq joinReq) throws BaseException;

	// 03. 회원정보 조회
	MemberDto lookup(Long memberId) throws BaseException;

	// 04. 휴대폰 인증 요청


	// 05. 휴대폰 인증 요청 확인
}
