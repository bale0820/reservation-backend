package com.example.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STUDIES")
@Getter
@NoArgsConstructor
@SequenceGenerator(
        name = "study_seq",
        sequenceName = "STUDY_SEQ",
        allocationSize = 1
)
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "study_seq")
    private Long id;

    @Column(nullable = false)
    private String modality;

    @Column(name = "BODY_PART", nullable = false)
    private String bodyPart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    private List<DicomImage> dicomImages = new ArrayList<>();

    public Study(String modality, String bodyPart, User user) {
        this.modality = modality;
        this.bodyPart = bodyPart;
        this.user = user;
    }

    // 🔥 연관관계 편의 메서드 (정답)
    public void addDicomImage(DicomImage dicomImage) {
        dicomImages.add(dicomImage);
        dicomImage.setStudy(this);
    }
}
