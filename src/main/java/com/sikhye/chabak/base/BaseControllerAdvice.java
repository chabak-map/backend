package com.sikhye.chabak.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice(basePackages = "com.sikhye.univcity")
public class BaseControllerAdvice {

	@ExceptionHandler(value = {ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
	public BaseResponse<BaseResponseStatus> errorInvalidPathHandler() {
		return new BaseResponse<>(BaseResponseStatus.INVALID_URI_PATH);
	}

	@ExceptionHandler(value = {MissingServletRequestParameterException.class, BindException.class})
	public BaseResponse<BaseResponseStatus> errorMissingParamHandler() {
		return new BaseResponse<>(BaseResponseStatus.EMPTY_PARAMETER);
	}
}
