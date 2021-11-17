package com.sikhye.chabak.base;


import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
	/**
	 * 1000 : 요청 성공
	 */
	SUCCESS(true, 1000, "요청에 성공하였습니다."),


	/**
	 * 2000 : Request 오류
	 */
	// Common
	REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
	EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
	INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
	INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
	CHECK_PASSWORD(false,2004,"비밀번호가 일치하지 않습니다."),
	CHECK_USER(false,2005,"유효하지 않은 계정입니다."),
	INVALID_URI_PATH(false, 2006, "URI 경로가 잘못되었습니다."),
	EMPTY_PARAMETER(false, 2007, "파라미터 값을 확인해주세요."),
	CHECK_CV(false,2008,"유효하지 않은 이력서입니다."),
	EMPTY_OR_INVALID_TOKEN(false, 2009, "발급된 토큰이 없거나 파싱 오류입니다."),

	// Common 형식 관련: 2100~
	INVALID_EMAIL(false, 2101, "올바른 이메일 형식을 입력해주세요."),
	INVALID_PASSWORD(false, 2102, "영문 대소문자, 숫자, 특수문자를 3가지 이상으로 조합하여 8자 이상 입력해 주세요."),
	INVALID_PHONE(false, 2102, "올바른 전화번호 형식을 입력해주세요."),

	// [POST] null 관련: 2200~
	POST_EMPTY(false, 2200, "필수 요소를 모두 확인해주세요."),
	POST_EMPTY_EMAIL(false, 2201, "이메일은 필수정보입니다."),
	POST_EMPTY_NAME(false, 2202, "이름은 필수정보입니다."),
	POST_EMPTY_PHONE(false, 2203, "전화번호는 필수정보입니다."),
	POST_EMPTY_PASSWORD(false, 2204, "비밀번호는 필수정보입니다."),
	POST_EMPTY_GROUPIDX(false, 2205, "직군을 선택해주세요."),
	POST_EMPTY_JOBIDX(false, 2206, "직무를 하나 이상 선택해주세요."),
	POST_EMPTY_PURPOSE(false, 2207, "고민을 선택해주세요."),
	POST_EMPTY_KEYWORD(false, 2208, "키워드를 하나 이상 선택해주세요."),
	POST_EMPTY_ALL(false, 2209, "하나 이상의 값을 입력해주세요."),
	POST_EMPTY_CV(false, 2210, "하나 이상의 이력서를 등록해주세요." ),
	//POST_EMPTY_EMAIL(false, 2201, "이메일은 필수정보입니다."),
	//POST_EMPTY_EMAIL(false, 2201, "이메일은 필수정보입니다."),

	USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

	// [POST] /users
	POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
	POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
	POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),


	/**
	 * 3000 : Response 오류
	 */
	// Common
	RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
	RESPONSE_EMPTY(false, 3001, "조회할 정보가 없습니다."),

	// [POST]
	DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
	FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
	FAILED_TO_MAKE_CV(false,3015,"이력서 생성에 실패했습니다."),

	// [PATCH]
	DELETE_MAIN_CV(false,3100,"메인 이력서는 삭제할 수 없습니다."),


	/**
	 * 4000 : Database, Server 오류
	 */
	DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
	SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

	//CREATE 오류 4100~
	CREATE_ERROR_CVDETAIL(false, 4101, "이력서 상세 정보 생성에 실패하였습니다."),

	//READ 오류 4200~
	RESPONSE_ERROR_USER(false, 4201, "해당 유저 정보를 불러오는데 실패하였습니다."),
	RESPONSE_ERROR_JOBS(false, 4202, "직무/직군 정보를 불러오는데 실패하였습니다."),
	RESPONSE_ERROR_CV(false, 4203, "이력서 정보를 불러오는데 실패하였습니다."),
	RESPONSE_ERROR_LEVEL2(false, 4204, "직무/직군 조회 전 직무/직군을 등록해주세요."),
	RESPONSE_ERROR_LEVEL3(false, 4205, "학력/경력 조회 전 학력/경력을 등록해주세요."),

	//UPDATE 오류 4200~
	UPDATE_FAIL_CVCAREER(false, 4201, "경력 등록에 실패했습니다."),
	UPDATE_FAIL_CVCAREERACCOMP(false, 4202, "경력 성과 등록에 실패했습니다."),
	UPDATE_FAIL_CVINFO(false, 4203, "이력서 기본정보 등록에 실패했습니다."),
	UPDATE_FAIL_CVEDUCATION(false, 4204, "학력 등록에 실패했습니다."),
	UPDATE_FAIL_CVSKILL(false, 4205, "스킬 등록에 실패했습니다."),
	UPDATE_FAIL_CVADDITIONAL(false, 4206, "기타 수상 내역 등록에 실패했습니다."),
	UPDATE_FAIL_CVLANGUAGE(false, 4207, "외국어 등록에 실패했습니다."),
	UPDATE_FAIL_CVLANGUAGETEST(false, 4208, "외국험 시험 등록에 실패했습니다."),
	UPDATE_FAIL_CVLINK(false, 4209, "링크 등록에 실패했습니다."),
	MODIFY_FAIL_CVMAIN(false,4210,"메인 이력서 등록에 실패했습니다."),
	MODIFY_FAIL_USER(false,4211,"유저 기본정보 수정에 실패했습니다."),

	//DELETE 오류 4300~
	DELETE_FAIL_CV(false,4301,"이력서 삭제에 실패했습니다."),
	DELETE_FAIL_CVDETAIL(false,4302,"이력서 상세항목 삭제에 실패했습니다."),
	MODIFY_FAIL_USERNAME(false,4303,"유저네임 수정 실패"),


	PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
	PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");




	// 5000 : 필요시 만들어서 쓰세요
	// 6000 : 필요시 만들어서 쓰세요


	private final boolean isSuccess;
	private final int code;
	private final String message;

	private BaseResponseStatus(boolean isSuccess, int code, String message) {
		this.isSuccess = isSuccess;
		this.code = code;
		this.message = message;
	}
}
