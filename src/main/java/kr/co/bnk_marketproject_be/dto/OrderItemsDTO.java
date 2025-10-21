package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemsDTO {
    private int id;
    private int orders_id;
    private int products_id;
    private int product_options_id;
    private int quantity;
    private int price;

    private String productName;  // ★ 상품명
    // ✅ 추가 (mapper에서 pi.url AS url 매핑)
    private String url;
    private String company_name; // ✅ 추가

    private String seller_rep;   // 판매자 대표자명
    private String seller_tel;   // 판매자 전화번호 (선택)
    private String seller_email; // 판매자 이메일 (선택)


}
