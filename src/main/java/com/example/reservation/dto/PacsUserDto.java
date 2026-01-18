package com.example.reservation.dto;

// PacsUserDto
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PacsUserDto {

    public Long userId;
    public String name;
    public String email;
    public List<StudyDto> studies = new ArrayList<>();

    public PacsUserDto(Long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}
