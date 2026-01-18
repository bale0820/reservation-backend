package com.example.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DicomImageDto {

    public Long imageId;
    public String dicomUrl;
}