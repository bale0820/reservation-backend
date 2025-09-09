package com.example.reservation;


import com.example.reservation.security.CustomOAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {


    private final JwtFilter jwtFilter;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    public SecurityConfig(JwtFilter jwtFilter, CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler; // ✅ 주입
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(crsf -> crsf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/login", "/api/register/**", "/api/reservations/**", "/api/upload/analyze", "/api/results", "/api/gpt/**", "/login/**", "/oauth2/**").permitAll().anyRequest().authenticated())
                .oauth2Login(oauth -> oauth     // ✅ OAuth2 로그인 활성화
                        .successHandler(customOAuth2SuccessHandler)// 로그인 성공 후 리다이렉트 URL
                )
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }


    // ✅ CORS 허용 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000","http://127.0.0.1:3000")); // 프론트 도메인
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // 쿠키/헤더 인증 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
