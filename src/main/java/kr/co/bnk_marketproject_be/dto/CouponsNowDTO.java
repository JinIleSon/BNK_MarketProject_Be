package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponsNowDTO {

    private int id;
    private String issue_id;
    private String status;
    private String used_at;
    private int coupons_id;
    private int users_id;
    private String company;
    private String note;

    // 추가 필드 - user_id(users 테이블) - 유저 아이디 가져오기
    private String user_id;

    // 추가 필드 - code, discount_type, coupon_name(coupons 테이블)
    private String code;
    private String discount_type;
    private String coupon_name;

    // 모달창용
    private int discount_value;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
}
