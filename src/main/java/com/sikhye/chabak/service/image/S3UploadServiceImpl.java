package com.sikhye.chabak.service.image;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sikhye.chabak.global.exception.BaseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadServiceImpl implements UploadService {

	private final AmazonS3 amazonS3;
	private final S3Component component;

	@Override
	public String uploadImage(MultipartFile file, String folderName) {
		String filename = createFilename(file.getOriginalFilename());
		String pathWithName = folderName + filename;
		log.info("S3_Upload_Filename = {}", pathWithName);

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(file.getSize());
		objectMetadata.setContentType(file.getContentType());

		try {
			InputStream inputStream = file.getInputStream();
			uploadFile(inputStream, objectMetadata, pathWithName);
		} catch (IOException e) {
			throw new BaseException(S3_UPLOAD_ERROR);
		}
		return getFileUrl(pathWithName);
	}

	@Override
	public List<String> uploadImages(List<MultipartFile> files, String folderName) {

		return files.stream()
			.map(file -> uploadImage(file, folderName))
			.collect(Collectors.toList());

	}

	@Override
	public Boolean deleteImage(String key) {
		try {
			DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(component.getBucket(), key);
			amazonS3.deleteObject(deleteObjectRequest);

			log.info("[S3 WARN] {} deleted", key);    // images/posts/3/3-1.png

			return true;
		} catch (Exception e) {
			throw new BaseException(S3_UPLOAD_ERROR);
		}
	}

	// ========== internal use ========== //
	private String createFilename(String originalFilename) {
		return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
	}

	private String getFileExtension(String filename) {
		try {
			return filename.substring(filename.lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e) {
			throw new BaseException(S3_FORMAT_ERROR);
		}

	}

	private void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String pathWithName) {
		amazonS3.putObject(new PutObjectRequest(component.getBucket(), pathWithName, inputStream, objectMetadata)
			.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	private String getFileUrl(String pathWithName) {
		return amazonS3.getUrl(component.getBucket(), pathWithName).toString();
	}
}

