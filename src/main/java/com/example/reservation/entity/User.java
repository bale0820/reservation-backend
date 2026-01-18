package com.example.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "user_seq",
        sequenceName = "USER_SEQ",
        allocationSize = 1
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    private String profileImage;

    private String provider;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
