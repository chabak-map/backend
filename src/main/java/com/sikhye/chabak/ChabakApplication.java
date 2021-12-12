package com.sikhye.chabak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.sikhye.chabak.global.config.ConfigProperties;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(value = {ConfigProperties.class})
public class ChabakApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChabakApplication.class, args);
	}

}
