package com.sikhye.chabak.utils.sms.config;

import com.sikhye.chabak.utils.sms.entity.SmsCacheKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableCaching
public class CacheConfig {

	private final RedisConnectionFactory redisConnectionFactory;
	private final StringRedisTemplate redisTemplate;


	@Bean
	public CacheManager cacheManager() {

		log.info("============= cache =============");
		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
			.disableKeyPrefix()
			.disableCachingNullValues()
			.entryTtl(Duration.ofSeconds(SmsCacheKey.DEFAULT_EXPIRE_SEC))
			.computePrefixWith(CacheKeyPrefix.simple())
			.serializeKeysWith(RedisSerializationContext
				.SerializationPair
				.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext
				.SerializationPair
				.fromSerializer(new StringRedisSerializer()));

		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

		// ptpt 01. SMS 캐시 설정을 해줘야 적용됨
		// https://newbedev.com/spring-boot-caching-with-redis-key-have-xac-xed-x00-x05t-x00-x06
		// 캐시매니저를 만들어도 문제가 발생한 경우 :: SMS라는 캐시에도 default처럼 설정해줬어야함
		log.info("============= SMS cache config =============");
		cacheConfigurations.put(SmsCacheKey.SMS,
			RedisCacheConfiguration.defaultCacheConfig()
				.disableKeyPrefix()
				.disableCachingNullValues()
				.entryTtl(Duration.ofSeconds(SmsCacheKey.SMS_EXPIRE_SEC))
				.computePrefixWith(CacheKeyPrefix.simple())
				.serializeKeysWith(RedisSerializationContext
					.SerializationPair
					.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext
					.SerializationPair
					.fromSerializer(new StringRedisSerializer())));


		return RedisCacheManager.RedisCacheManagerBuilder
			.fromConnectionFactory(redisConnectionFactory)
			.cacheDefaults(configuration)
			.withInitialCacheConfigurations(cacheConfigurations).build();

	}


}
