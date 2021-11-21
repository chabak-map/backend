package com.sikhye.chabak.utils.sms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
// >> pt04. basepackge 정해줘야 다른 패키지에서 redis repo 사용 가능
@EnableRedisRepositories(basePackages = "com.sikhye.chabak")
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

//	private final Long expireTime = 600L;


	// connect와 관련된 객체
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisHost, redisPort);
	}

	//	 실제로 template 역할하여 키-값을 직렬화하여 데이터 변환
	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(redisConnectionFactory());

		return redisTemplate;
	}


	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
		stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
		stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
		return stringRedisTemplate;
	}

//	@Bean
//	// Serializer가 빠지면 \xac\xed\x00\x05t\x00\x06 유니코드 값이 들어간다.
//	public RedisCacheManager redisCacheManager() {
//		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
//			.disableCachingNullValues()
//			.entryTtl(Duration.ofSeconds(SmsCacheKey.DEFAULT_EXPIRE_SEC))
//			.computePrefixWith(CacheKeyPrefix.simple())
//			.serializeKeysWith(RedisSerializationContext
//				.SerializationPair
//				.fromSerializer(new StringRedisSerializer()));
//
//		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//
//		cacheConfigurations.put(SmsCacheKey.SMS,
//			RedisCacheConfiguration.defaultCacheConfig()
//				.entryTtl(Duration.ofSeconds(SmsCacheKey.SMS_EXPIRE_SEC)));
//
//		return RedisCacheManager.RedisCacheManagerBuilder
//			.fromConnectionFactory(redisConnectionFactory())
//			.cacheDefaults(configuration)
//			.withInitialCacheConfigurations(cacheConfigurations).build();
//	}


//	// Cache 관련 설정
//	@Bean
//	public RedisCacheConfiguration cacheConfiguration() {
//
//		log.info("Info -> Redis Cache Configuration");
//
//		RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//			.entryTtl(Duration.ofSeconds(expireTime))
//			.disableCachingNullValues()
//			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//		cacheConfig.usePrefix();
//
//		log.info("Duration -> " + cacheConfig.getTtl().getSeconds());
//
//		return cacheConfig;
//	}


//	@Bean
//	public RedisCacheManager cacheManager() {
//
//		log.info("Info -> Redis Cache Manager");
//
//		return RedisCacheManager
//			.builder(this.redisConnectionFactory())
//			.cacheDefaults(this.cacheConfiguration())
//			.build();
//	}

}
