package com.sikhye.chabak.utils.sms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sikhye.chabak.utils.sms.dto.MessagesRequestDto;
import com.sikhye.chabak.utils.sms.dto.SendSmsResponseDto;
import com.sikhye.chabak.utils.sms.dto.SmsRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Getter
@RequiredArgsConstructor
@Service
@Transactional
public class SmsService {

	@Value("${naver.cloud.ACCESS_KEY_ID}")
	private String accessKey;

	@Value("${naver.cloud.SECRET_KEY}")
	private String secretKey;

	@Value("${naver.cloud.SERVICE_ID}")
	private String serviceId;

	public SendSmsResponseDto sendSms(String recipientPhoneNumber, String content) throws ParseException, JsonProcessingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
		Long time = System.currentTimeMillis();
		List<MessagesRequestDto> messages = new ArrayList<>();

		// 보내는 사람에게 내용을 보냄.
		messages.add(new MessagesRequestDto(recipientPhoneNumber, content)); // content부분이 내용임 /

		// / 전체 json에 대해 메시지를 만든다.
		SmsRequestDto smsRequestDto = new SmsRequestDto("SMS", "COMM", "82", "01066146729", "MangoLtd", messages);

		// 쌓아온 바디를 json 형태로 변환시켜준다.
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = objectMapper.writeValueAsString(smsRequestDto);

		// 헤더에서 여러 설정값들을 잡아준다.
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-ncp-apigw-timestamp", time.toString());
		headers.set("x-ncp-iam-access-key", accessKey);

		// 제일 중요한 signature 서명하기.
		String sig = makeSignature(time);
		System.out.println("sig -> " + sig);
		headers.set("x-ncp-apigw-signature-v2", sig);

		// 위에서 조립한 jsonBody와 헤더를 조립한다.
		HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);
		System.out.println(body.getBody());

		// restTemplate로 post 요청을 보낸다. 별 일 없으면 202 코드 반환된다.
		RestTemplate restTemplate = new RestTemplate();
		SendSmsResponseDto sendSmsResponseDto = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages"), body, SendSmsResponseDto.class);
		System.out.println(sendSmsResponseDto.getStatusCode());
		return sendSmsResponseDto;
	}


	public String makeSignature(Long time) throws
		NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		String space = " ";                    // one space
		String newLine = "\n";                    // new line
		String method = "POST";                    // method
		String url = "/sms/v2/services/" + serviceId + "/messages";    // url (include query string)
		String timestamp = time.toString();            // current timestamp (epoch)

		String message = new StringBuilder()
			.append(method)
			.append(space)
			.append(url)
			.append(newLine)
			.append(timestamp)
			.append(newLine)
			.append(accessKey)
			.toString();

		SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);

		byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
		String encodeBase64String = Base64.encodeBase64String(rawHmac);

		return encodeBase64String;
	}
}
