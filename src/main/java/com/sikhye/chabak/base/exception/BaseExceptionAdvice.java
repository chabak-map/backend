package com.sikhye.chabak.base.exception;

import com.sikhye.chabak.base.BaseResponse;
import com.sikhye.chabak.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class BaseExceptionAdvice {

	@ExceptionHandler(BaseException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected BaseResponse<BaseResponseStatus> customException(BaseException baseException) {
		BaseResponseStatus status = baseException.getStatus();
		log.warn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + " : " + status.getMessage());
		return new BaseResponse<>(status);
	}

}
