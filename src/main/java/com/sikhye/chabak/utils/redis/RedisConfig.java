package com.sikhye.chabak.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
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

	// 실제로 template 역할하여 키-값을 직렬화하여 데이터 변환
	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());

		// key 값은 String 값으로 출력
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		// value 값은 객체를 Json 형식으로 출력
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

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
