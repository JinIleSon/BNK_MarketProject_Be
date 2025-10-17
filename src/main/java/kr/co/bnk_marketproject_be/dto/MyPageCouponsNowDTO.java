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
public class MyPageCouponsNowDTO {

    private int id;
    private String coupon_name;
    private String discount_value;
    private String note;
    private String status;
    private LocalDateTime valid_to;
    private String user_id;


}
