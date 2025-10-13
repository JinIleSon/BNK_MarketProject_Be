package kr.co.bnk_marketproject_be.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdminBannerDTO {

    private Long id;                   // PK

    private String name;               // 배너명 (NOT NULL)
    private String size;         // 배너 크기 (예: 1200×80)
    private String bgColor;            // 배경색 (#ffffff)
    private String linkUrl;            // 링크 URL
    private String bnrPosition;        // 배너 위치 (MAIN1, FOOTER1 등)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;        // 노출 시작일

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;          // 노출 종료일

    private String timeFrom;           // 노출 시작시간 (예: 09:00)
    private String timeTo;             // 노출 종료시간 (예: 21:00)

    private String imagePath;          // 이미지 경로 (/uploads/banner/...)
    private String status;             // 상태 (ACTIVE / INACTIVE)

    private LocalDateTime createdAt;   // 생성일
    private LocalDateTime updatedAt;   // 수정일
    private LocalDateTime visibleFrom; // 실제 노출 시작시각 (date+time 합성 가능)
    private LocalDateTime visibleTo;   // 실제 노출 종료시각

    // ⚙️ 업로드용
    private MultipartFile file;        // 업로드 파일 (폼에서 전송되는 이미지)
}
