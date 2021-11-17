package com.sikhye.chabak.utils.redis;

import com.sikhye.chabak.base.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sikhye.chabak.base.BaseResponseStatus.DATABASE_ERROR;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

	private final TestRedisRepository testRedisRepository;

	// RedisRepository
	public String redisRepositorySave() throws BaseException {
		Address address = new Address("서울시 금천구");
		RedisTest redisTest = RedisTest
			.builder()
			.id(null)
			.address(address)
			.name("이름")
			.build();

		RedisTest savedRedisTest = testRedisRepository.save(redisTest);
		Optional<RedisTest> findId = testRedisRepository.findById(savedRedisTest.getId());

		if (findId.isPresent()) {
			return findId.get().getId();
		} else {
			throw new BaseException(DATABASE_ERROR);
		}


	}
}
