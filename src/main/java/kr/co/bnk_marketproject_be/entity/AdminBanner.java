package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "BANNER")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class AdminBanner {

    // ====== enum을 엔티티 안에 중첩 선언 ======
    public enum BannerPosition { MAIN1, MAIN2, PRODUCT1, MEMBER1, MYPAGE1 }
    public enum BannerStatus   { ACTIVE, INACTIVE }


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120, nullable = false)
    private String name;

    // 충돌 피한 새 컬럼명으로 매핑
    @Column(name = "BNR_POSITION")
    @Enumerated(EnumType.STRING)
    private BannerPosition position;

    @Column(name = "BNR_SIZE")
    private String size;



    @Column(name = "BG_COLOR", length = 16)
    private String bgColor;

    @Column(name = "LINK_URL", length = 512)
    private String linkUrl;


    @Column(name="VISIBLE_FROM")
    private LocalDateTime visibleFrom;

    @Column(name="VISIBLE_TO")
    private LocalDateTime visibleTo;



    @Column(length = 1024)
    private String imagePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BannerStatus status;

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(nullable = false)
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        var now = java.time.LocalDateTime.now();
        createdAt = now; updatedAt = now;
        if (status == null) status = BannerStatus.INACTIVE;
        if (linkUrl == null) linkUrl = "";
    }

    @PreUpdate
    void onUpdate() { updatedAt = java.time.LocalDateTime.now(); }

    private static final java.time.format.DateTimeFormatter DF =
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final java.time.format.DateTimeFormatter TF =
            java.time.format.DateTimeFormatter.ofPattern("HH:mm");

    private static LocalDate parseLocalDate(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalDate.parse(s, DF); }
        catch (Exception e) { throw new IllegalArgumentException("날짜 형식은 yyyy-MM-dd 이어야 합니다."); }
    }
    private static LocalTime parseLocalTime(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalTime.parse(s, TF); }
        catch (Exception e) { throw new IllegalArgumentException("시간 형식은 HH:mm 이어야 합니다."); }
    }

}



