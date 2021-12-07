package com.sikhye.chabak.global.exception;

import com.sikhye.chabak.global.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends RuntimeException {
	private BaseResponseStatus status;
}
