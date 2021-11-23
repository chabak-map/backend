//package com.sikhye.chabak.utils.aws;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//public class BasicUploadController {
//
//	private final BasicUploadService basicUploadService;
//
//	@PostMapping("/api/v1/upload")
//	public String uploadImage(@RequestPart MultipartFile file) {
//		return basicUploadService.uploadImage(file);
//	}
//
//	@PostMapping("/api/v1/multi-upload")
//	public List<String> uploadImages(@RequestPart List<MultipartFile> file) {
//		return basicUploadService.uploadImages(file);
//	}
//}
