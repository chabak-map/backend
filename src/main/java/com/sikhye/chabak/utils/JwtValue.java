package com.sikhye.chabak.utils;

public enum JwtValue {
	X_ACCESS_TOKEN;

	public String toString() {
		return name().replaceAll("_", "-");
	}
}
