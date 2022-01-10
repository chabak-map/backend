package com.sikhye.chabak.utils.encrypt;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.sikhye.chabak.global.config.ConfigProperties;
import com.sikhye.chabak.global.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EncryptServiceAes256 implements EncryptService {

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	private final ConfigProperties properties;
	private final String key;
	private final String iv;

	private Cipher cipher;
	private SecretKeySpec keySpec;
	private IvParameterSpec ivParamSpec;

	public EncryptServiceAes256(ConfigProperties properties) {
		this.properties = properties;
		this.key = properties.getUserInfoPasswordKey();
		this.iv = key.substring(0, 16);
		setCipherSpec();
	}

	@Override
	public String encrypt(String text) throws Exception {
		this.cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
		byte[] encrypted = this.cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encrypted);
	}

	@Override
	public String decrypt(String cipherText) throws Exception {
		this.cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
		byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
		byte[] decrypted = this.cipher.doFinal(decodedBytes);
		return new String(decrypted, StandardCharsets.UTF_8);
	}

	private void setCipherSpec() {
		try {
			this.cipher = Cipher.getInstance(ALGORITHM);
			this.keySpec = new SecretKeySpec(iv.getBytes(), "AES");
			this.ivParamSpec = new IvParameterSpec(iv.getBytes());
		} catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
			throw new BaseException(AES256_NO_SUCH_SPEC);
		}
	}
}

