package com.sikhye.chabak.utils.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderService {

	private final JavaMailSender javaMailSender;

	@Async
	public void sendEmail(SimpleMailMessage email) {
		javaMailSender.send(email);
	}
}

