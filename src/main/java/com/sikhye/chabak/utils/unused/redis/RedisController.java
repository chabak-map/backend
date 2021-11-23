//package com.sikhye.chabak.utils.redis;
//
//import com.sikhye.chabak.base.exception.BaseException;
//import com.sikhye.chabak.base.BaseResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//public class RedisController {
//
//	private final RedisService redisService;
//
//	@GetMapping("/redis-save")
//	public BaseResponse<String> redisSaveTest() {
//		try {
//			String resultId = redisService.redisRepositorySave();
//			return new BaseResponse<>(resultId);
//		} catch (BaseException exception) {
//			return new BaseResponse<>(exception.getStatus());
//		}
//	}
//}
