package com.example.reservation;

import com.example.reservation.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody User user) {
        System.out.println("안녕하세요"+user.getUserId());
        if (userRepository.findByemail(user.getEmail()).isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "이미 존재하는 이메일입니다.", null));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse<>(true, "회원가입 성공!", null));
    }

    @PostMapping("/registerNaver")
    public ResponseEntity<ApiResponse<Void>> registerNaver(@RequestBody User user, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(token);
        if (userRepository.findByemail(userEmail).isPresent()) {
            userRepository.updatePhoneByEmail(userEmail, user.getPhone());
            return ResponseEntity.ok(new ApiResponse<>(true, "회원가입 성공!", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(false, "존재하는 이메일이 없습니다.", null));
    }

    @PostMapping("/register/checkUserId")
    public ResponseEntity<ApiResponse<Void>> checkUserId(@RequestBody User user) {
        System.out.println(userRepository.findByUserId(user.getUserId()));
        if (userRepository.findByUserId(user.getUserId()).isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(false, null, null));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(true, null, null));
        }

    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User loginUser) {
//        Optional<User> user = userRepository.findByemail(loginUser.getEmail());
//
//        if(user ==null || !passwordEncoder.matches(loginUser.getPassword(),user.getPassword())) {
//            return ResponseEntity.status(401).body(new ApiResponse<>(false, "이메일 또는 비밀번호가 틀렸습니다.", null));
//        }
//        String token = jwtUtil.generateToken(user.getEmail());
//
//        return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", token));
//    }

        Optional<User> optionalUser = userRepository.findByUserId(loginUser.getUserId());

        if (optionalUser.isEmpty() ||
                !passwordEncoder.matches(loginUser.getPassword(), optionalUser.get().getPassword())) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, "이메일 또는 비밀번호가 틀렸습니다.", null));
        }

        User user = optionalUser.get();
        String token = jwtUtil.generateToken(user.getEmail());
        System.out.println(user.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", token));
    }


}
