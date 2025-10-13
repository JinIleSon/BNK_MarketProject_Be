package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCTS")
public class Products {

    @Id
    private int id;

    private int product_code;
    private int sellers_id;
    private int categories_id;
    private String product_name;
    private String description;
    private int price;
    private int stock;
    private String status;
    private String created_at;
    private String updated_at;
    private int discount;
    private int hits;
    private int point;

    // 추가 필드 - 페이지네이션 + user
    @Transient
    private String name;
    @Transient
    private String userId;
    @Transient
    private String email;
    @Transient
    private String phone;
    @Transient
    private String address;
    @Transient
    private String role;

    public void setName(String name) {
        this.name = name;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setRole(String role) {
        this.role = role;
    }

    // 추가 필드 - url(이미지 불러오기, product_images 테이블)
    @Transient
    private String url;

    //추가 필드 - 판매자 이름
    @Transient
    private String user_name;
}
