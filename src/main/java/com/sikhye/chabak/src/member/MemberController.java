package com.sikhye.chabak.src.member;

import com.sikhye.chabak.base.BaseResponse;
import com.sikhye.chabak.base.exception.BaseException;
import com.sikhye.chabak.src.member.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/test")
	public BaseResponse<String> test() {
		return new BaseResponse<>("helloV4");
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
	public BaseResponse<Long> editMemberInform(@Valid EditMemberReq editMemberReq) {
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

	// ptpt: RequestBody 를 새로운 객체 생성 없이도 사용이 가능
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
