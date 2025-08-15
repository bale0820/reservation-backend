package com.example.reservation;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_result")
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filname;
    private String diagnosis;
    private double confidence;
    private LocalDateTime createdaAt;
    private String userEmail;

    public AnalysisResult() {}

    public AnalysisResult(String filename, String diagnosis,  double confidence, String userEmail) {
        this.filname = filename;
        this.diagnosis = diagnosis;
        this.confidence = confidence;
        this.userEmail = userEmail;
        this.createdaAt = LocalDateTime.now();
        this.userEmail = userEmail;
    }

    public Long getId() {
        return id;
    }
    public String getFilname() {
        return filname;
    }

    public void setFilname(String filname) {
        this.filname = filname;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public LocalDateTime getCreatedaAt() {
        return createdaAt;
    }

    public void setCreatedaAt(LocalDateTime createdaAt) {
        this.createdaAt = createdaAt;
    }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
