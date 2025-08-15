package com.example.reservation;

import com.example.reservation.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpHeaders; // ✅ 이걸로 수정

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private AnalysisResultRepository analysisRepo;

    @Value("${file.upload-dir}")
    private  String uploadDir;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<Map<String,Object>>> uploadAndAnalyze(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authHeader) {
        if(file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "파일이 비어 있습니다.", null));
        }

        try {
            String filepath = uploadDir + "/" + file.getOriginalFilename();
            File dir = new File(uploadDir);
            if(!dir.exists()) {
                dir.mkdirs();
            }
            file.transferTo(new File(filepath));

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            FileSystemResource resource = new FileSystemResource(filepath);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =  new HttpEntity<>(body, headers);
            String flaskUrl = "http://localhost:5000/analyze";
            ResponseEntity<Map> response =  restTemplate.postForEntity(flaskUrl, requestEntity, Map.class);


            Map<String,Object> result = response.getBody();

            String token = authHeader.replace("Bearer", "").trim();
            String userEmail = jwtUtil.extractEmail(token);



            String filename = (String)result.get("filename");
            String diagnosis = (String)result.get("diagnosis");
            double confidence = Double.parseDouble(result.get("confidence").toString());

            AnalysisResult analysis = new AnalysisResult(filename,diagnosis, confidence, userEmail);
            analysisRepo.save(analysis);

            return ResponseEntity.ok(new ApiResponse<>(true, "AI 분석 성공", result));



        }
        catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(false, "분석 실패: " + e.getMessage(), null));
        }
    }





}
