package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPageSubmitDTO {
    private Integer coupon_id;            // null 가능
    private int usePoint;                // 0 가능
    private String payment_method;        // 필수
    private OrderPageShippingInfoDTO shipping;    // 필수
}
