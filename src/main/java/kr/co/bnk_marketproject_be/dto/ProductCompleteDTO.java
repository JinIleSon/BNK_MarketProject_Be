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
public class ProductCompleteDTO {
    private int id;          // orders.id
    private String orderCode;
    private Integer orderId;             // ★ 템플릿 호환용(추가)
    private String payMethod;          // payments.method
    private String buyerName;          // 주문자 이름(표시용)
    private String buyerPhone;         // 주문자 연락처(표시용)

    private OrderPageShippingInfoDTO shipping;  // 실제 배송지
    private List<OrderPageLineRowDTO> items;
    private OrderPageSummaryDTO summary;

    private String completedAt;        // 완료일시 표시용
}

