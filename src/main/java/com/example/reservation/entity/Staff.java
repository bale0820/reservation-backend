package com.example.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "STAFF")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_seq")
    @SequenceGenerator(
            name = "staff_seq",
            sequenceName = "STAFF_SEQ",
            allocationSize = 1
    )
    @Column(name = "STAFF_ID") // ✅ PK
    private Long id;

    @Column(name = "LOGIN_ID", nullable = false, unique = true)
    private String loginId; // ✅ 로그인용 ID

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private StaffRole role;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
