package com.example.reservation.service;

import com.example.reservation.entity.User;
import com.example.reservation.repository.UserRepository;

import java.util.Map;

public class CustomOAuth2Service {
    private final UserRepository userRepository;

    public CustomOAuth2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveOrUpdate(Map<String, Object> response) {
        String email = (String) response.get("email");
        String name = (String) response.get("name");

        return userRepository.findByemail(email)
                .map(user -> {
                    user.setName(name); // 이름 업데이트
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    return userRepository.save(newUser);
                });
    }
}
