package com.sikhye.chabak.utils.redis;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

	private String address;

	public Address(String address) {
		this.address = address;
	}
}
