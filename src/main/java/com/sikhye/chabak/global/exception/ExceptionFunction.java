package com.sikhye.chabak.global.exception;

// >> ptpt : 람다 exception 처리
public interface ExceptionFunction<T, R> {
	R apply(T r) throws Exception;
}
