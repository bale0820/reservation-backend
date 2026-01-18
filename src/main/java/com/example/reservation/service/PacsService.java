package com.example.reservation.service;

import com.example.reservation.dto.DicomImageDto;
import com.example.reservation.dto.PacsUserDto;
import com.example.reservation.dto.StudyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PacsService {

    private final JdbcTemplate jdbcTemplate;

    public List<PacsUserDto> getPacsData() {

        // 🔥 Oracle + JDBC 충돌 방지용 alias (ID 절대 안 겹치게)
        String sql = """
            SELECT
                u.id        AS U_ID,
                u.name      AS USER_NAME,
                u.email     AS USER_EMAIL,

                s.id        AS S_ID,
                s.modality  AS MODALITY,
                s.body_part AS BODY_PART,

                d.id        AS D_ID,
                d.dicom_url AS DICOM_URL
            FROM users u
            JOIN studies s 
              ON s.user_id = u.id
            LEFT JOIN dicom_images d 
              ON d.study_id = s.id
            ORDER BY u.id, s.id, d.id
        """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        // 🔎 디버깅용 (필요 없으면 삭제)
         rows.forEach(System.out::println);

        // 🔥 USER 기준으로 묶기 (중복 제거 + 순서 유지)
        Map<Long, PacsUserDto> userMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {

            /* =========================
               1️⃣ USER
            ========================= */

            Long userId = ((Number) row.get("U_ID")).longValue();

            PacsUserDto user = userMap.computeIfAbsent(userId, id ->
                    new PacsUserDto(
                            id,
                            (String) row.get("USER_NAME"),
                            (String) row.get("USER_EMAIL")
                    )
            );

            /* =========================
               2️⃣ STUDY
            ========================= */

            Long studyId = ((Number) row.get("S_ID")).longValue();

            StudyDto study = user.getStudies().stream()
                    .filter(s -> s.getStudyId().equals(studyId))
                    .findFirst()
                    .orElseGet(() -> {
                        StudyDto s = new StudyDto(
                                studyId,
                                (String) row.get("MODALITY"),
                                (String) row.get("BODY_PART")
                        );
                        user.getStudies().add(s);
                        return s;
                    });

            /* =========================
               3️⃣ DICOM IMAGE (LEFT JOIN)
            ========================= */

            if (row.get("D_ID") != null) {
                study.getSeries().add(
                        new DicomImageDto(
                                ((Number) row.get("D_ID")).longValue(),
                                (String) row.get("DICOM_URL")
                        )
                );
            }
        }

        return new ArrayList<>(userMap.values());
    }
}
