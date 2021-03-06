package com.sikhye.chabak;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sikhye.chabak.global.config.ConfigProperties;
import com.sikhye.chabak.global.config.SmsConfigProperties;
import com.sikhye.chabak.service.oauth.config.OAuthProperties;

// >> ptpt: ElasticSearch repository 는 스캔하지 않아야 빈 에러가 나타나지 않음
//20211216
// @EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(
// 	type = FilterType.ASSIGNABLE_TYPE,
// 	classes = {PlaceSearchRepository.class, PlaceTagSearchRepository.class,
// 		PostSearchRepository.class, PostTagSearchRepository.class}
// ))
@EnableJpaRepositories
@EnableScheduling
@EnableBatchProcessing
@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(value = {ConfigProperties.class, OAuthProperties.class, SmsConfigProperties.class})
public class ChabakApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChabakApplication.class, args);
	}

}
