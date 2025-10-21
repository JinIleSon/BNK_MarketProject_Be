package kr.co.bnk_marketproject_be.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MypageInquiryDTO {
    private Long id;
    private Long userId;
    private Long sellerId;
    private Long orderItemId;
    private String qtype;
    private String email;
    private String subject;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
