package kr.co.bnk_marketproject_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MypageExchangeRequestDTO {
    private Long id;                 // PK (IDENTITY)
    private Long orderItemId;        // ORDER_ITEMS.id
    private Long userId;             // USERS.id
    private String reasonText;       // 교환 사유
    private String desiredOption;    // 교환 희망 옵션
    private String evidenceUrls;     // 사진 URL 문자열
    private String reasonCode;       // 단순교환 / 파손 / 주문실수 / 기타
    private String status;           // REQUESTED / APPROVED / REJECTED / COMPLETED
    private String createdAt;        // 생성일
}
