package com.example.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ANALYSIS_RESULT")
@Getter
@NoArgsConstructor
@SequenceGenerator(
        name = "analysis_result_seq",
        sequenceName = "ANALYSIS_RESULT_SEQ",
        allocationSize = 1
)
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysis_result_seq")
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String diagnosis;

    @Column(nullable = false)
    private double confidence;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private String userEmail;
    private String userName;

    public AnalysisResult(String filename, String diagnosis, double confidence, String userName) {
        this.filename = filename;
        this.diagnosis = diagnosis;
        this.confidence = confidence;
        this.userName = userName;
    }
}
