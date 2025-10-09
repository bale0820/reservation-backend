//package com.example.reservation;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Service
//@RequiredArgsConstructor
//public class S3Service {
//
//    private final AmazonS3 amazonS3;
//    private final String bucketName = "my-reservation-images"; // ✅ 본인 버킷명으로 변경
//
//    public String uploadFile(MultipartFile file) throws IOException {
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(file.getContentType());
//        metadata.setContentLength(file.getSize());
//
//        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
//
//        return amazonS3.getUrl(bucketName, fileName).toString(); // 업로드된 파일 URL 반환
//    }
//}
