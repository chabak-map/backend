package com.sikhye.chabak.service.jwt.constant;

public enum JwtValue {
	X_ACCESS_TOKEN("X-ACCESS-TOKEN");

	public final String label;

	JwtValue(String label) {
		this.label = label;
	}
}
