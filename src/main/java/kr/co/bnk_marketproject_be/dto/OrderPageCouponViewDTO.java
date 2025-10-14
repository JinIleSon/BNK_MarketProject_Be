package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderPageCouponViewDTO {
    private int id;
    private String coupon_name;          // coupons.coupon_name
    private String discount_type;  // PERCENT/FIXED 등
    private int discount_value;    // %
    private String company;       // 발급사(표시용)
    private String note;          // 유의사항(표시용)
    private String valid_from;     // 화면 표시용
    private String valid_to;       // 화면 표시용
    private String status;        // USABLE/EXPIRED/USED 등
}
