package com.sikhye.chabak.utils.aws;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BasicUploadService {
	String uploadImage(MultipartFile file);

	List<String> uploadImages(List<MultipartFile> file);
}
