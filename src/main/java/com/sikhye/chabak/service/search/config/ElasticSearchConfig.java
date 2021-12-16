// package com.sikhye.chabak.service.search.config;
//
// import org.elasticsearch.client.RestHighLevelClient;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.elasticsearch.client.ClientConfiguration;
// import org.springframework.data.elasticsearch.client.RestClients;
// import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
// // AbstractElasticsearchConfiguration를 상속받아서 접속정보 부분은 재정의
// @EnableElasticsearchRepositories
// @Configuration
// public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
//
// 	@Override
// 	public RestHighLevelClient elasticsearchClient() {
// 		ClientConfiguration clientConfiguration = ClientConfiguration.builder()
// 			.connectedTo("localhost:9200")    // TODO: configureProperties 등록
// 			.build();
// 		return RestClients.create(clientConfiguration).rest();
// 	}
// }
