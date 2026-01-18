package com.example.reservation.controller;

import com.example.reservation.config.JwtUtil;
import com.example.reservation.config.S3Uploader;
import com.example.reservation.dto.ApiResponse;
import com.example.reservation.entity.AnalysisResult;
import com.example.reservation.entity.User;
import com.example.reservation.repository.AnalysisResultRepository;
import com.example.reservation.repository.UserRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders; // ✅ 이걸로 수정

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private AnalysisResultRepository analysisRepo;

//    @Value("${file.upload-dir}")
//    private  String uploadDir;

    @Value("${openai.api-key}")
    private String apiKey;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private S3Uploader s3Uploader;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<AnalysisResult>> uploadAndAnalyze(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authHeader) {

        try {
            // 1️⃣ JWT 검증
            String token = authHeader.replace("Bearer", "").trim();
            String userEmail = jwtUtil.extractEmail(token);
            Optional<User> user = userRepository.findByemail(userEmail);
            User userData = null;
            if(!user.isEmpty()) userData = user.get();
            // 2️⃣ S3 업로드 (🔥 로컬 저장 없음)
            String s3Key = s3Uploader.upload(file);
            String s3Url = s3Uploader.getFileUrl(s3Key);

            // 3️⃣ GPT 분석 (URL 또는 file_id 기반)
            Map<String, Object> response = analyzeFileWithGPT(s3Url);


            List<?> output = (List<?>) response.get("output");
            Map<?, ?> message = (Map<?, ?>) output.get(0);


            List<?> content = (List<?>) message.get("content");
            Map<?, ?> outputText = (Map<?, ?>) content.get(0);


            String jsonText = outputText.get("text").toString();


            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> parsed = mapper.readValue(jsonText, Map.class);


            String diagnosis = parsed.get("diagnosis").toString();
            double confidence = Double.parseDouble(parsed.get("confidence").toString());


            // 5️⃣ DB 저장
            AnalysisResult analysis = new AnalysisResult(s3Key,diagnosis, confidence, userData.getName());
            analysisRepo.save(analysis);

            return ResponseEntity.ok(new ApiResponse<>(true, "AI 분석 성공", analysis));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "분석 실패", null));
        }
    }


    public String uploadFileToOpenAI(File file) {

        String url = "https://api.openai.com/v1/files";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("purpose", "assistants"); // 필수
        body.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return response.getBody().get("id").toString(); // 🔑 file_id
    }

    public Map<String, Object> analyzeFileWithGPT(String s3ImageUrl) {

        String url = "https://api.openai.com/v1/responses";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("model", "gpt-4.1", "input", new Object[]{Map.of("role", "user", "content", new Object[]{Map.of("type", "input_text", "text", """
                너는 의료 영상 판독을 보조하는 AI이다.
                
                아래에 제공된 흉부 X-ray 이미지를 분석하고,
                의학적 소견을 요약하여 JSON 형식으로만 응답하라.
                
                반드시 아래 JSON 형식만 반환해야 한다.
                다른 텍스트는 절대 포함하지 마라.
                
                {
                  "diagnosis": "의학적 소견 요약",
                  "confidence": 0.0
                }
                """), Map.of("type", "input_image", "image_url", s3ImageUrl)})});

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(url, new HttpEntity<>(requestBody, headers), Map.class);

        return response.getBody();
    }


}
