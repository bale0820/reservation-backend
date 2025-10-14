package com.example.reservation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products") // 실제 DB 테이블 이름
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @Column(nullable = false, length = 100)
    private String name; // 상품명

    @Column(length = 100)
    private String category; // 카테고리 (예: 과일, 채소, 수산물 등)

    private int price; // 가격

    @Column(length = 1000)
    private String imageUrl; // 이미지 URL

    private String description; // 상품 설명
}
