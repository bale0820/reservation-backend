package com.example.reservation;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

//    @GetMapping("/user")
//    public Object user(Authentication authentication) {
//        // 로그인한 사용자 정보 확인
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        // 네이버 응답 JSON은 "response" 안에 들어 있음
//        Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
//
//        String id = (String) response.get("id");
//        String email = (String) response.get("email");
//        String name = (String) response.get("name");
//        String profileImage = (String) response.get("profile_image");
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("id", id);
//        result.put("email", email);
//        result.put("name", name);
//        result.put("profileImage", profileImage);
//
//        return result;
//    }
private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public User user(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");

        String email = (String) response.get("email");
        String name = (String) response.get("name");
        String profileImage = (String) response.get("profile_image");

        // DB에 있으면 update, 없으면 새로 저장
        return userRepository.findByemail(email)
                .map(u -> {
                    u.setName(name);
                    u.setProfileImage(profileImage);
                    return userRepository.save(u);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setProfileImage(profileImage);
                    newUser.setProvider("naver");
                    return userRepository.save(newUser);
                });
    }

}
