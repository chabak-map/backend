package com.sikhye.chabak.utils.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadServiceImpl implements BasicUploadService {

	private final AmazonS3 amazonS3;
	private final S3Component component;

	@Override
	public String uploadImage(MultipartFile file) {
		String filename = createFilename(file.getOriginalFilename());

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(file.getSize());
		objectMetadata.setContentType(file.getContentType());

		try {
			InputStream inputStream = file.getInputStream();
			uploadFile(inputStream, objectMetadata, filename);
		} catch (IOException e) {
			throw new IllegalArgumentException("파일변환 중 에러 발생하였습니다.");
		}
		return getFileUrl(filename);
	}

	@Override
	public List<String> uploadImages(List<MultipartFile> file) {
		return file.stream()
			.map(this::uploadImage)
			.collect(Collectors.toList());
	}


	// ========== internal use ========== //
	private String createFilename(String originalFilename) {
		return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
	}

	private String getFileExtension(String filename) {
		try {
			return filename.substring(filename.lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e) {
			throw new IllegalArgumentException(String.format("잘못된 형식의 파일입니다."));
		}
	}

	private void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String filename) {
		amazonS3.putObject(new PutObjectRequest(component.getBucket(), filename, inputStream, objectMetadata)
			.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	private String getFileUrl(String filename) {
		return amazonS3.getUrl(component.getBucket(), filename).toString();
	}
}
