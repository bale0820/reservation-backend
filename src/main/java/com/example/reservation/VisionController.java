package com.example.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vision")
public class VisionController {

    private final VisionService visionService;
    private final ProductRepository productRepository;

    @PostMapping("/analyze")
    public List<Product> analyzeImage(@RequestParam("file") MultipartFile file) throws Exception {
        String labels = visionService.detectLabels(file);
        if (labels == null || labels.isBlank()) {
            return List.of(); // 빈 리스트 반환
        }

        String firstLabel = labels.split(",")[0].trim();
        System.out.println("Label" +  labels);
        System.out.println("firstLabel" +  firstLabel);
        return productRepository.findByDescriptionContainingIgnoreCase(firstLabel);
    }
}
