package com.sikhye.chabak.utils.sms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmsServiceTest {

	@Autowired
	private SmsService smsService;

	@Test
	void sendSms() throws UnsupportedEncodingException, ParseException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
		smsService.sendSms("01066146729", "sms테스트");
	}

}
