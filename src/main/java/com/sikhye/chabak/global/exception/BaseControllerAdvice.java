package com.sikhye.chabak.global.exception;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;

import java.util.Objects;

import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.global.response.BaseResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages = "com.sikhye.chabak")
public class BaseControllerAdvice {

	@ExceptionHandler(value = {ConstraintViolationException.class})
	public BaseResponse<BaseResponseStatus> errorInvalidPathHandler(ConstraintViolationException cve) {

		return new BaseResponse<>(INVALID_URI_PATH);
	}

	@ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
	public BaseResponse<BaseResponseStatus> errorInvalidPathHandler(MethodArgumentTypeMismatchException mat) {

		return new BaseResponse<>(INVALID_URI_PATH);
	}

	@ExceptionHandler(value = {MissingServletRequestParameterException.class})
	public BaseResponse<BaseResponseStatus> errorInvalidPathHandler(MissingServletRequestParameterException msrpe) {

		return new BaseResponse<>(INVALID_URI_PATH);

	}

	@ExceptionHandler(value = {BindException.class})
	public BaseResponse<BaseResponseStatus> errorInvalidPathHandler(BindException be) {

		BindingResult bindingResult = be.getBindingResult();

		String bindCode = Objects.requireNonNull(bindingResult.getFieldError()).getCode();

		switch (bindCode) {
			case "NotBlank":
				return new BaseResponse<>(VALID_INPUT_BLANK);
		}

		return new BaseResponse<>(INVALID_URI_PATH);
	}

	@ExceptionHandler(value = {BaseException.class})
	protected BaseResponse<BaseResponseStatus> handleCustomException(BaseException e) {
		log.error("handleCustomException throw CustomException : {}", e.status);
		return new BaseResponse<>(e.status);
	}

}
