package com.sikhye.chabak.service.post.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingReq {

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	private List<MultipartFile> images;

	@Builder
	public PostingReq(String title, String content, List<MultipartFile> images) {
		this.title = title;
		this.content = content;
		this.images = images;
	}

	public PostingReq(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setImages(List<MultipartFile> images) {
		this.images = images;
	}

	public PostingReq() {
	}

	public Boolean isEmptyOrNullImages() {
		if (images == null) {
			return true;
		} else {
			return images.isEmpty();
		}
	}
}
