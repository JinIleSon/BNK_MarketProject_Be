package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageLineRowDTO {
    private int order_item_id;
    private int id;          // products.id
    private int product_code;        // products.product_code
    private String product_name;     // products.product_name
    private String option_name;      // product_options.option_name (없으면 null)
    private int quantity;           // order_items.quantity
    private int unitPrice;          // products.price (정상가)
    private int discountRate;       // products.discount(%) 없으면 0
    private int lineTotal;          // 소계 = (unitPrice - 할인) * quantity
    private String url;        // product_images.url (대표)
    private Integer delichar;       // 배송비(건별), null이면 0로 처리
    private boolean freeShipping;   // delichar==null || delichar==0
}
