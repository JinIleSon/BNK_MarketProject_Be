package kr.co.bnk_marketproject_be.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOpsStatsDTO {

    /** 막대 차트 (일자별) */
    private List<DailyOpsDTO> dailyStats;

    /** 파이 차트 (카테고리별 매출) */
    private List<CategorySalesDTO> categorySales;
    private long totalSales;

    /** 카드형 지표 */
    private long pendingPayment;   // 입금대기
    private long shippingReady;    // 배송준비
    private long cancelRequest;    // 취소요청
    private long exchangeRequest;  // 교환요청
    private long returnRequest;    // 반품요청
    private long orderCount;       // 주문건수
    private long orderAmount;      // 주문금액(원)
    private long userSignups;      // 회원가입 수
    private long visitors;         // 방문자 수(원하면 추후 로그 기반으로 대체)
    private long qnaCount;         // 문의 게시물 수

    // ===== 내부 DTO =====
    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DailyOpsDTO {
        private LocalDate date;   // 2025-10-10
        private long orders;      // 주문 수
        private long payments;    // 결제 수
        private long cancels;     // 취소 수
    }

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CategorySalesDTO {
        private String category;  // "가전"
        private long amount;      // 매출
    }
}
