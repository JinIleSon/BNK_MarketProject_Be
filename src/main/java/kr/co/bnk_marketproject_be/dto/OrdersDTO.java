package kr.co.bnk_marketproject_be.dto;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersDTO {

    private int id;
    private int users_id;
    private String status;
    private int total_amount;
    private String created_at;
    private String updated_at;
    private String order_code;

    public String getCreated_at() {
        if (created_at == null) return null;
        if (created_at.length() < 19) return created_at;
        return created_at.substring(0, 19);
    }

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
    @Transient
    private String company_name;

    private List<OrderItemsDTO> orderItems;

    @Transient
    private String seller_id;           // SELLERS.SELLER_ID
    @Transient
    private String seller_rep;          // USERS.NAME
    @Transient
    private String seller_tel;          // USERS.PHONE
    @Transient
    private String seller_email;        // USERS.EMAIL
    @Transient
    private String seller_bizno;        // SELLERS.BIZ_REGISTRATION_NUMBER
    @Transient
    private String seller_mailnum;      // SELLERS.MAIL_ORDER_NUMBER
    @Transient
    private String seller_addr;         // USERS.ADDRESS + USERS.DETAILADDRESS
    private String delivery_company;
    private String invoice;
    private String delivery_status;
    private String delivery_phone;
    private int payment_amount;



}
