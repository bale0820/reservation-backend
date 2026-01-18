package com.example.reservation.repository;

import com.example.reservation.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {

    @Query("""
        select distinct s
        from Study s
        join fetch s.user
        left join fetch s.dicomImages
    """)
    List<Study> findAllWithUserAndDicom();
}