package kr.co.bnk_marketproject_be.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMemberDTO {

    private int id;
    private String rep;
    private String gender;
    private String grade;
    private String point;
    private String tel;
    private String created_at;
    private String look;

    // 추가 필드(board)
    private String zipcode;
    private String address1;
    private String address2;
    private String updated_at;
    private String content;

    public String getCreated_at() {
        if (created_at == null) return null;
        return (created_at.length() < 19) ? created_at : created_at.substring(0, 19);
    }

    // 추가 필드
    @Column(name = "BOARD_TYPE")
    private String boardType;

    // 추가 필드 - 페이지네이션
    @Transient
    private String name;
    @Column(name = "user_id")
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
