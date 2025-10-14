package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPageSummaryDTO {
    private int itemCount;        // 상품 수
    private int subtotalAmount;   // 정상가 합계
    private int discountAmount;   // 총 할인금액(정가-판매가)
    private int couponAmount;     // 쿠폰 할인(체크아웃/완료에서만)
    private int pointUse;         // 사용 포인트(체크아웃/완료에서만)
    private int delichar;      // 배송비
    private int totalPayable;     // 최종 결제금액
    private int rewardPoint;      // 적립 포인트
}
