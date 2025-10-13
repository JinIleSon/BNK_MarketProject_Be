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
@Table(name = "POINT_LEDGERS")
public class AdminPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String users_id;
    private int change_amount;
    private int balance;
    private String description;
    private String created_at;

    // 추가 필드
    @Column(name = "BOARD_TYPE")
    private String boardType;

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
