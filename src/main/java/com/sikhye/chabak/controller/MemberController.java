package com.sikhye.chabak.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.service.member.MemberService;
import com.sikhye.chabak.service.member.dto.EditMemberReq;
import com.sikhye.chabak.service.member.dto.JoinReq;
import com.sikhye.chabak.service.member.dto.LoginReq;
import com.sikhye.chabak.service.member.dto.LoginRes;
import com.sikhye.chabak.service.member.dto.MemberDto;
import com.sikhye.chabak.service.member.dto.PasswordReq;
import com.sikhye.chabak.service.sms.dto.SmsReq;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping("/test")
	public BaseResponse<String> test() {
		return new BaseResponse<>("helloV2022-01-04");
	}

	@PostMapping("/login")
	public BaseResponse<LoginRes> login(@Valid @RequestBody LoginReq loginReq) {

		return new BaseResponse<>(memberService.login(loginReq));
	}

	@PostMapping
	public BaseResponse<LoginRes> join(@Valid @RequestBody JoinReq joinReq) {
		try {
			return new BaseResponse<>(memberService.join(joinReq));
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
	}

	@GetMapping
	public BaseResponse<MemberDto> lookup() {
		return new BaseResponse<>(memberService.lookup());
	}

	@PostMapping("/sms")
	public BaseResponse<String> requestPhoneAuth(@Valid @RequestBody SmsReq smsReq) {
		try {
			return new BaseResponse<>(memberService.requestPhoneAuth(smsReq.getPhoneNumber()));
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
	}

	@PostMapping("/sms/verify")
	public BaseResponse<Boolean> verifySms(@Valid @RequestBody SmsReq smsReq) {
		try {
			return new BaseResponse<>(memberService.verifySms(smsReq.getVerifyCode(), smsReq.getPhoneNumber()));
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
	}

	@PostMapping("/image")
	public BaseResponse<String> uploadImage(@RequestPart MultipartFile memberImage) {

		return new BaseResponse<>(memberService.uploadImage(memberImage));

	}

	@PatchMapping
	public BaseResponse<Long> editMemberInform(@Valid @RequestBody EditMemberReq editMemberReq) {
		return new BaseResponse<>(memberService.editMemberInform(editMemberReq));
	}

	@PatchMapping("/password")
	public BaseResponse<Long> editMemberPassword(@Valid @RequestBody PasswordReq passwordReq) {
		return new BaseResponse<>(memberService.editPassword(passwordReq));
	}

	@PostMapping("/help/email")
	public BaseResponse<String> findMemberEmail(@RequestBody Map<String, String> param) {
		return new BaseResponse<>(memberService.findMemberEmail(param.get("phoneNumber")));
	}

	@PostMapping("/help/password")
	public BaseResponse<Long> findMember(@RequestBody Map<String, String> param) {
		return new BaseResponse<>(memberService.findMember(param.get("phoneNumber"), param.get("email")));
	}

	@PatchMapping("/status")
	public BaseResponse<Long> statusToDeleteMember() {
		return new BaseResponse<>(memberService.statusToDeleteMember());
	}

	@PostMapping("/check/nickname")
	public BaseResponse<Boolean> checkDuplicatedNickname(@RequestBody Map<String, String> param) {
		return new BaseResponse<>(memberService.isDuplicatedNickname(param.get("nickname")));
	}

	@PostMapping("/check/email")
	public BaseResponse<Boolean> checkDuplicatedEmail(@RequestBody Map<String, String> param) {
		return new BaseResponse<>(memberService.isDuplicatedEmail(param.get("email")));
	}

}
