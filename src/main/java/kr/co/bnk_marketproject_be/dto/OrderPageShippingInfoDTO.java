package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPageShippingInfoDTO {
    private String recipient;
    private String hp;
    private String zipcode;
    private String address;
    private String address2;
    private String memo;          // 배송메모(선택)
}
