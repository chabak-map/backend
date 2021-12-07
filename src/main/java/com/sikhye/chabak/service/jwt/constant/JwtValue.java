package com.sikhye.chabak.service.jwt.constant;

public enum JwtValue {
	X_ACCESS_TOKEN;

	public String toString() {
		return name().replaceAll("_", "-");
	}
}
