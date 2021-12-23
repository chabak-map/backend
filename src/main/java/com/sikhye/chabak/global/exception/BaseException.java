package com.sikhye.chabak.global.exception;

import com.sikhye.chabak.global.response.BaseResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
	BaseResponseStatus status;

	// public BaseResponse<BaseResponseStatus> getBaseException(BaseResponseStatus status) {
	// 	// super(status.name());
	// 	// this.status = status;
	// 	return new BaseResponse<>(status);
	// }

	// public BaseException(BaseResponseStatus status) {
	// 	super(status.getMessage());
	// }
}
