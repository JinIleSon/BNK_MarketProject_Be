package kr.co.bnk_marketproject_be.dto;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 손진일이 편집함 2025/10/05 오후 07:56

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminInquiryDTO {

    private Long id;
    private String board_type;
    private String board_type2;
    private String board_type3;
    private String title;

    @Column(name = "user_id")
    private String userId;

    private String content;
    private int hits;
    private int look;
    private String qnaStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @PrePersist
    private void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}