package com.example.reservation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/results")
public class AnalysisResultController {

    @Autowired
    private AnalysisResultRepository analysisRepo;

    @Autowired
    private JwtUtil jwtUtil;



    @GetMapping
    public List<AnalysisResult> getUserResults(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(token);
        return analysisRepo.findByUserEmail(userEmail);
    }

}
