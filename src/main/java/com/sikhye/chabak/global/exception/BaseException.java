package com.sikhye.chabak.global.exception;

import com.sikhye.chabak.global.response.BaseResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
	BaseResponseStatus status;
}
