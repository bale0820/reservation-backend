package com.example.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DICOM_IMAGES")
@Getter
@NoArgsConstructor
@SequenceGenerator(
        name = "dicom_seq",
        sequenceName = "DICOM_SEQ",
        allocationSize = 1
)
public class DicomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dicom_seq")
    private Long id;

    @Column(name = "DICOM_URL", nullable = false, length = 500)
    private String dicomUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDY_ID", nullable = false)
    private Study study;

    public DicomImage(String dicomUrl) {
        this.dicomUrl = dicomUrl;
    }

    // 🔥 연관관계 주인 setter
    void setStudy(Study study) {
        this.study = study;
    }
}

