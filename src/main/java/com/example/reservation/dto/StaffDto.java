package com.example.reservation.dto;

import com.example.reservation.entity.StaffRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class StaffDto {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String userId;
    private StaffRole role;
    private String department;
}
