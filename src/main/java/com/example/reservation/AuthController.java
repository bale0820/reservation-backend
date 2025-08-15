package com.example.reservation;

import com.example.reservation.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;



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

        if (userRepository.findByemail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "이미 존재하는 이메일입니다.", null));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse<>(true, "회원가입 성공!", null));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User loginUser) {
        User user = userRepository.findByemail(loginUser.getEmail());

        if(user ==null || !passwordEncoder.matches(loginUser.getPassword(),user.getPassword())) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "이메일 또는 비밀번호가 틀렸습니다.", null));
        }
        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", token));
    }


}
