package com.sikhye.chabak.global.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.sikhye.chabak.service.sms.entity.SmsCacheKey;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableCaching
public class CacheConfig {

	private final RedisConnectionFactory redisConnectionFactory;

	@Bean
	public CacheManager cacheManager() {

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
