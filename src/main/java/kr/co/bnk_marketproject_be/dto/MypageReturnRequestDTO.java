package kr.co.bnk_marketproject_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MypageReturnRequestDTO {
    private Long id;                 // PK (IDENTITY)
    private Long orderItemId;        // ORDER_ITEMS.id
    private Long userId;             // USERS.id
    private String reasonText;       // 반품 사유
    private String evidenceUrls;     // 사진 URL 문자열 (여러개면 , 로 연결)
    private String pickupAddress;     // 반품 회수 주소
    private String reasonCode;       // 단순변심 / 파손 / 주문실수 / 기타
    private String status;           // REQUESTED / APPROVED / REJECTED / COMPLETED
    private String createdAt;        // 생성일
}
