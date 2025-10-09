//package com.example.reservation;
//
//import com.example.reservation.S3Service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/uploadImage")
//public class S3Controller {
//
//    private final S3Service s3Service;
//
//    @PostMapping
//    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
//        return s3Service.uploadFile(file);
//    }
//}
