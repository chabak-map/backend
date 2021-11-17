package com.sikhye.chabak.src.email;

import com.sikhye.chabak.base.BaseException;
import com.sikhye.chabak.src.email.model.EmailToken;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sikhye.chabak.base.BaseResponseStatus.DATABASE_ERROR;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailTokenService {

	private final EmailTokenRepository emailTokenRepository;
	private final MailSenderService mailSenderService;

	// 이메일 인증 토큰 생성
//	@Cacheable	// 캐시힛 용도로 사용하는듯
//	@TimeToLive
//	@Cacheable(value = "EmailToken")
	public String createEmailToken(Long memberId, String receiverEmail) {

		Assert.notNull(memberId, "memberId는 필수입니다");
		Assert.hasText(receiverEmail, "receiverEmail은 필수입니다.");

		// 이메일 토큰 저장
		EmailToken emailToken = EmailToken.builder()
			.id(null)
			.memberId(memberId)
			.build();

		emailTokenRepository.save(emailToken);

		// 이메일 전송
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(receiverEmail);
		mailMessage.setSubject("회원가입 이메일 인증");
		mailMessage.setText("http://univ-city.shop:9000/confirm-email?token=" + emailToken.getId());
		mailSenderService.sendEmail(mailMessage);

		return emailToken.getId();    // 인증메일 전송 시 토큰 반환

	}

	public EmailToken findEmailToken(String token) throws BaseException {
		Optional<EmailToken> findToken = emailTokenRepository.findById(token);

		return findToken.orElseThrow(() -> new BaseException(DATABASE_ERROR));
	}


}
