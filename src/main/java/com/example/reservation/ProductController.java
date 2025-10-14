package com.example.reservation;

import com.example.reservation.Product;
import com.example.reservation.ProductRepository;
import com.example.reservation.S3Service;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@PermitAll
//@CrossOrigin(origins = "*")
public class ProductController {

    private final S3Service s3Service;
    private final ProductRepository productRepository;

    // 🖼️ 이미지 업로드 + DB 저장
    @PostMapping("/upload")
    public Product uploadProduct(
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam("description") String description
    ) throws IOException {

        // 1️⃣ S3 업로드
        String imageUrl = s3Service.uploadFile(file);

        // 2️⃣ DB 저장
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        product.setDescription(description);
        return productRepository.save(product);
    }
}
