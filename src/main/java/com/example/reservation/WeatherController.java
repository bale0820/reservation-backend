package com.example.reservation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "http://localhost:3000")
public class WeatherController {

    @Value("${kma.api.key}")
    private String apiKey;

    @GetMapping("/daily")
    public ResponseEntity<String> getDailyWeather() {
        String url = "https://apihub.kma.go.kr/api/typ02/openApi/FmlandWthrInfoService/getDayStatistics";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("authKey", apiKey)
                .queryParam("ST_YMD", "20240901")
                .queryParam("ED_YMD", "20240901")
                .queryParam("AREA_ID", "442100000")        // ✅ 서산시
                .queryParam("PA_CROP_SPE_ID", "PA130301")  // ✅ 마늘
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 10)
                .queryParam("dataType", "JSON");

        RestTemplate rest = new RestTemplate();
        String response = rest.getForObject(builder.toUriString(), String.class);

        return ResponseEntity.ok(response);
    }


}
