package com.example.reservation;

import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class VisionService {

    public String detectLabels(MultipartFile file) throws IOException {
        // Vision API 클라이언트 생성
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // 업로드된 이미지 바이트 변환
            ByteString imgBytes = ByteString.copyFrom(file.getBytes());
            Image img = Image.newBuilder().setContent(imgBytes).build();

            // 라벨 감지 요청 (LABEL_DETECTION)
            Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feature)
                    .setImage(img)
                    .build();

            // API 호출
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(List.of(request));
            List<AnnotateImageResponse> responses = response.getResponsesList();

            // 결과 라벨 텍스트 추출
            StringBuilder result = new StringBuilder();
            for (AnnotateImageResponse res : responses) {
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    result.append(annotation.getDescription()).append(", ");
                }
            }

            return result.toString(); // 예: "apple, fruit, food, ..."
        }
    }
}
