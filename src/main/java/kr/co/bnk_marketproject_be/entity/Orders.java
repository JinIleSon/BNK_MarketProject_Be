package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "ORDERS")
public class Orders {

    @Id
    private int id;

    private int users_id;
    private String status;
    private int total_amount;
    private String created_at;
    private String updated_at;
    private String order_code;

    // 추가 필드 - users
    @Transient
    private String userId;
    @Transient
    private String user_name;

    // 추가 필드 - payments
    @Transient
    private String method;

    // 추가 필드 - count(orders_id)
    @Transient
    private int orders_count;

    // 주문상세 추가 필드
    @Transient
    private String name;
    @Transient
    private String phone;
    @Transient
    private int quantity;
    @Transient
    private int product_code;
    @Transient
    private String product_name;
    @Transient
    private BigDecimal price;
    @Transient
    private int discount;
    @Transient
    private int delichar;
    @Transient
    private String url;
    @Transient
    private String ruphone;
    @Transient
    private String suname;
    @Transient
    private String recipient;
    @Transient
    private String address;
    @Transient
    private String postcode;
    @Transient
    private String user_address;
    @Transient
    private String detail_address;
}
