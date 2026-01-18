package com.example.reservation.repository;

import com.example.reservation.entity.DicomImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DicomImageRepository extends JpaRepository<DicomImage, Long> {

    @Query("""
        select d
        from DicomImage d
        where d.study.id in :studyIds
    """)
    List<DicomImage> findByStudyIds(List<Long> studyIds);
}
