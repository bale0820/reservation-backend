package com.example.reservation.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class StudyDto {

    public Long studyId;
    public String modality;
    public String bodyPart;
    public List<DicomImageDto> series = new ArrayList<>();

    public StudyDto(Long studyId, String modality, String bodyPart) {
        this.studyId = studyId;
        this.modality = modality;
        this.bodyPart = bodyPart;
    }
}
