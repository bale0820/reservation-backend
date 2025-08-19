package com.example.reservation;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ai_question")
public class AiQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;

    @Column(columnDefinition="TEXT")
    private String question;

    @Column(columnDefinition="LONGTEXT")
    private String answer;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    // getters/setters
}

