package com.sikhye.chabak.src.member;

import com.sikhye.chabak.base.BaseException;
import com.sikhye.chabak.base.BaseResponse;
import com.sikhye.chabak.src.member.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/test")
	public BaseResponse<String> test() {
		return new BaseResponse<>("hello");
	}


	@PostMapping("/login")
	public BaseResponse<LoginRes> login(@Valid @RequestBody LoginReq loginReq) {
		try {
			return new BaseResponse<>(memberService.login(loginReq));
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
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
		try {
			return new BaseResponse<>(memberService.lookup());
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
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


}
