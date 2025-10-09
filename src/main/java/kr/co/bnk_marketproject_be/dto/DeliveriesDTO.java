package kr.co.bnk_marketproject_be.dto;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveriesDTO {

    private int id;
    private int orders_id;
    private String address;
    private String status;
    private String shipped_at;
    private String delivered_at;
    private String invoice;
    private String delicom;
    private String recipient;
    private int delichar;
    private String receipt;
    private String note;

    public String getReceipt() {
        return receipt.substring(0, 19);
    }

    // 추가 필드 - order_code(주문번호), total_amount(물품합계) - orders 테이블
    @Transient
    private String order_code;

    @Transient
    private int total_amount;

    // 추가 필드 - product_name(상품명) - products 테이블
    @Transient
    private String product_name;

    // 추가 필드 - order_items의 주문 건수(count (*))
    @Transient
    private int item_count;

    // 배송상세를 위한 추가필드(7개의 테이블)
    @Transient
    private String url;
    @Transient
    private int product_code;
    @Transient
    private String phone;
    @Transient
    private String seller;
    @Transient
    private BigDecimal price;

}
