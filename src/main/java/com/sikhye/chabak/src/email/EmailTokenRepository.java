package com.sikhye.chabak.src.email;

import com.sikhye.chabak.src.email.model.EmailToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailTokenRepository extends CrudRepository<EmailToken, String> {
	Optional<EmailToken> findByMemberId(Long memberId);
}
