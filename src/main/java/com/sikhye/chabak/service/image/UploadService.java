package com.sikhye.chabak.service.image;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {
	String uploadImage(MultipartFile file, String folderName);

	List<String> uploadImages(List<MultipartFile> files, String folderName);
}
