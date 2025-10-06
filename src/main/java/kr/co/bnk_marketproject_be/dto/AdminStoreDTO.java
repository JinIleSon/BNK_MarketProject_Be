package kr.co.bnk_marketproject_be.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStoreDTO {
    private int id;

    // 추가 필드
    @Column(name="board_type")
    private String boardType;

    private String user_id;
    private String password;
    private String busname;
    private String rep;
    private String cornum;
    private String comnum;
    private String tel;
    private String manage;
    private String look;
    private String fax;
    private String zipcode;
    private String address1;
    private String address2;

    // 추가 필드 - 페이지네이션
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
}
