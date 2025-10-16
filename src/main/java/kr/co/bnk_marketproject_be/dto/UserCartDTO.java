package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCartDTO {

    private int id;
    private String user_id;
    private String url;
    private String product_name;
    private String description;
    private int quantity;
    private int price;
    private int discount_pct; // sql에서 계산하는 컬럼 -> as discount_pct
    private int point;
    private int delichar;
    private int total_price; // sql에서 계산하는 컬럼 -> as total_price
}
