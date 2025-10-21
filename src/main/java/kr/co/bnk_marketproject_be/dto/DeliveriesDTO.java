package kr.co.bnk_marketproject_be.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
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
    private String hp;
    private String recipient;
    private Integer delichar;
    private String receipt;
    private String memo;
    private String zipcode;
    private String address2;
    private String note;
    private String delicom;


    public String getReceipt() {
        if (receipt == null) return null;
        return receipt.substring(0, Math.min(19, receipt.length()));
    }

    // 추가 필드 - order_code(주문번호), total_amount(물품합계) - orders 테이블
    private String order_code;

    private int total_amount;

    // 추가 필드 - product_name(상품명) - products 테이블
    private String product_name;

    // 추가 필드 - order_items의 주문 건수(count (*))
    @Transient
    private int item_count;

    // 배송상세를 위한 추가필드(7개의 테이블)
    @Transient
    private String url;
    private int product_code;
    @Transient
    private String phone;
    @Transient
    private String seller;
    @Transient
    @Column(precision = 19, scale = 2)
    private BigDecimal price;
    @Transient
    private String suname;
    @Transient
    private BigDecimal discount;

}
