package com.sikhye.chabak.src.aws;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@ConfigurationProperties(prefix = "cloud.aws.s3")
@Component
public class S3Component {

	private String bucket;

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
}

