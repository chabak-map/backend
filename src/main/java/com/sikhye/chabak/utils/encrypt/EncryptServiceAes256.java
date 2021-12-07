package com.sikhye.chabak.utils.encrypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.sikhye.chabak.global.config.ConfigProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EncryptServiceAes256 implements EncryptService {

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	private final ConfigProperties properties;
	private final String key;
	private final String iv;

	public EncryptServiceAes256(ConfigProperties properties) {
		this.properties = properties;
		this.key = properties.getUserInfoPasswordKey();
		this.iv = key.substring(0, 16);
	}

	@Override
	public String encrypt(String text) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
		IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

		byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encrypted);
	}
	
	@Override
	public String decrypt(String cipherText) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
		IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

		byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
		byte[] decrypted = cipher.doFinal(decodedBytes);
		return new String(decrypted, StandardCharsets.UTF_8);
	}
}

