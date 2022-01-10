package com.sikhye.chabak.global.exception;

public interface ExceptionFunction<T, R> {
	R apply(T r) throws Exception;
}
