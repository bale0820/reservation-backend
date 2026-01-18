package com.example.reservation.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "AI_QUESTION")
@SequenceGenerator(
        name = "ai_question_seq",
        sequenceName = "AI_QUESTION_SEQ",
        allocationSize = 1
)
public class AiQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ai_question_seq")
    private Long id;

    @Column(nullable = false)
    private String userEmail;

    @Lob
    @Column(nullable = false)
    private String question;

    @Lob
    @Column(nullable = false)
    private String answer;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
