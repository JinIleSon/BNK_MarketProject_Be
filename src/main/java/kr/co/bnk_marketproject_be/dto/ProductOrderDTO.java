package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderDTO {
    private List<OrderPageLineRowDTO> items;
    private OrderPageSummaryDTO summary;

    // 할인/결제 영역
    private int availablePoint;                 // 보유 포인트
    private List<OrderPageCouponViewDTO> availableCoupons;
    private List<String> paymentMethods;        // ["신용카드","체크카드","계좌이체","휴대폰결제","카카오페이"...]

    // 기본 배송지
    private DeliveriesDTO defaultShipping;

    // 폼 바인딩(POST)
    private OrderPageSubmitDTO submit;
}
