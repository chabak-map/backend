package com.sikhye.chabak.service.search.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

/**
 * elasticsearchOperations : Query를 받아서 엘라스틱서치에 요청을 보내는 역할
 * 요청에서는 RestHighLevelClient를 사용
 * 엘라스틱서치 설정을 상속받아서 추가 변수, 함수를 만들어 자식 추상클래스로 만듦
 */
public abstract class AbstractElasticsearchConfiguration extends ElasticsearchConfigurationSupport {

	@Bean
	public abstract RestHighLevelClient elasticsearchClient();

	@Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
	public ElasticsearchOperations elasticsearchOperations(ElasticsearchConverter elasticsearchConverter,
		RestHighLevelClient elasticsearchClient) {

		ElasticsearchRestTemplate template = new ElasticsearchRestTemplate(elasticsearchClient, elasticsearchConverter);
		template.setRefreshPolicy(refreshPolicy());

		return template;
	}
}
