package com.example.reservation;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public S3Service(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.region.static}") String region
    ) {
        // ✅ 올바른 인증 객체 생성
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        // ✅ 반드시 V4 서명 프로토콜로 리전 설정
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        amazonS3.putObject(bucketName, fileName, file.getInputStream(), null);
        return amazonS3.getUrl(bucketName, fileName).toString(); // ✅ 업로드된 S3 URL 반환}
//
//        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
//
//        // ✅ 파일 메타데이터 설정 (브라우저에서 다운로드되지 않게)
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(file.getContentType()); // ★ 핵심!
//        metadata.setContentLength(file.getSize());
//
//        // ✅ 업로드 (퍼블릭으로)
//        PutObjectRequest putObjectRequest = new PutObjectRequest(
//                bucketName,
//                fileName,
//                file.getInputStream(),
//                metadata
//        ).withCannedAcl(CannedAccessControlList.PublicRead);
//
//        amazonS3.putObject(putObjectRequest);
//
//        // ✅ URL 반환
//        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
