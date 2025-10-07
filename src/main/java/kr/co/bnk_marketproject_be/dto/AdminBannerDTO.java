package kr.co.bnk_marketproject_be.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import kr.co.bnk_marketproject_be.entity.AdminBanner;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminBannerDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                     // PK

    @NotBlank(message = "배너 이름은 필수입니다.")
    private String name;                 // 배너이름

    @NotBlank(message = "배너 크기는 필수입니다.")
    private String size;                 // 예: 1200×80

    @NotBlank(message = "배경색은 필수입니다.")
    private String bgColor;              // #ffffff

    private String linkUrl;              // 배너링크 (선택)

    @NotBlank(message = "배너위치는 필수입니다.")
    private AdminBanner.BannerPosition position;             // MAIN1, MAIN2, ...

    // 날짜는 LocalDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)   // "yyyy-MM-dd"
    private LocalDate dateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;

    // 시간은 LocalTime
    @DateTimeFormat(pattern = "HH:mm")               // "HH:mm"
    private LocalTime timeFrom;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeTo;

    private String imagePath;            // 업로드된 이미지 경로
    private AdminBanner.BannerStatus status;            // ACTIVE / INACTIVE

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private MultipartFile file;          // 첨부파일

    // DTO에 계산필드 추가
    private boolean effectiveActive;
}
