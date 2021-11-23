package com.sikhye.chabak.base;

import com.sikhye.chabak.base.BaseResponseStatus;

import static com.sikhye.chabak.base.BaseResponseStatus.*;

public class ValidationExceptionProvider {

	public static BaseResponseStatus getExceptionStatus(String target) {
		switch (target) {
			case "email":
				return POST_USERS_INVALID_EMAIL;
			case "nickname":
				return POST_USERS_INVALID_NICKNAME;
			case "password":
				return POST_USERS_INVALID_PASSWORD;
		}
		return null;
	}

}
