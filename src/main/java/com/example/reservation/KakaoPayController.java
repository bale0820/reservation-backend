package com.example.reservation;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay")
@PermitAll
public class KakaoPayController {

    private static final String CID = "TC0ONETIME"; // 테스트용 CID
    private static final String ADMIN_KEY = "KakaoAK 발급받은_Admin_Key"; // 본인 키로 교체

    @PostMapping
    public ResponseEntity<?> kakaoPayReady() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        // ✅ MultiValueMap으로 변경
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "user123");
        params.add("item_name", "테스트 결제");
        params.add("quantity", "1");
        params.add("total_amount", "1000");
        params.add("vat_amount", "0");
        params.add("tax_free_amount", "0");
        params.add("approval_url", "http://localhost:3000/success");
        params.add("cancel_url", "http://localhost:3000/cancel");
        params.add("fail_url", "http://localhost:3000/fail");

        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "PRD7ED36427B322217B90AB0E361055929AE4143");
        headers.add("Authorization", "KakaoAK " + "PRD7ED36427B322217B90AB0E361055929AE4143"); // ⚠️ 공백 필수
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        URI uri = new URI("https://kapi.kakao.com/v1/payment/ready");
        ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);

        return ResponseEntity.ok(response.getBody());
    }
}
