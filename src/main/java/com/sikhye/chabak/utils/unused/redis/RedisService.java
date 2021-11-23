//package com.sikhye.chabak.utils.sms;
//
//import com.sikhye.chabak.base.exception.BaseException;
//import com.sikhye.chabak.base.BaseResponseStatus;
//import com.sikhye.chabak.utils.sms.entity.SmsToken;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.stereotype.Service;
//
//import static com.sikhye.chabak.base.BaseResponseStatus.*;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class RedisService {
//
//	private final RedisRepository redisRepository;
//
//	public void saveToken(String verifyCode, String phoneNumber) throws BaseException {
//		SmsToken smsToken = SmsToken.builder()
//			.verifyCode(verifyCode)
//			.phoneNumber(phoneNumber)
//			.build();
//
//		redisRepository.save(smsToken);
//	}
//
//	public String findCode(String phoneNumber) throws BaseException {
//
//		SmsToken smsToken = redisRepository.findById(phoneNumber).orElseThrow(() -> new BaseException(DATABASE_ERROR));
//
//		return smsToken.getVerifyCode();
//
//	}
//
////	@CachePut(cacheNames = "")
//
////	public void deleteCode(String phoneNumber) throws BaseException {
////
////	}
//
//
//	// RedisRepository
////	public String redisRepositorySave() throws BaseException {
////		Address address = new Address("서울시 금천구");
////		RedisTest redisTest = RedisTest
////			.builder()
////			.id(null)
////			.address(address)
////			.name("이름")
////			.build();
////
////		RedisTest savedRedisTest = redisRepository.save(redisTest);
////		Optional<RedisTest> findId = redisRepository.findById(savedRedisTest.getId());
////
////		if (findId.isPresent()) {
////			return findId.get().getId();
////		} else {
////			throw new BaseException(DATABASE_ERROR);
////		}
////	}
//
//
//}
