package com.sikhye.chabak.base.exception;

import com.sikhye.chabak.base.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends RuntimeException {
	private BaseResponseStatus status;
}
