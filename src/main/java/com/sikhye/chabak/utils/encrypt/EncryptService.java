package com.sikhye.chabak.utils.encrypt;

public interface EncryptService {

	String encrypt(String text) throws Exception;

	String decrypt(String cipherText) throws Exception;
}
