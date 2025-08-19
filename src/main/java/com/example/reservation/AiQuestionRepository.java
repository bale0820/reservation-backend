package com.example.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiQuestionRepository extends JpaRepository<AiQuestion, Long> {
    List<AiQuestion> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}
