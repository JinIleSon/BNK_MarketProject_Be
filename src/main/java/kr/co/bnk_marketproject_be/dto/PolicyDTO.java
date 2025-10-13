package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyDTO {

    private String terms_buyer;
    private String terms_seller;
    private String terms_finance;
    private String terms_location;
    private String terms_privacy;

}
