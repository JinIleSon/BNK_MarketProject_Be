package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="USERS")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // DB 컬럼과의 매핑시 필요해서 넣음
    @Column(name = "user_id")
    private String userId;
    private String password;
    private String name;
    private String gender;
    private String birth;
    private String email;
    private String phone;
    private String postcode;
    private String address;
    private String detailAddress;

    @Builder.Default
    private String role = "user";

    // 생성시 자동
    @CreationTimestamp
    private String created_at;
    // 수정시 자동
    @UpdateTimestamp
    private String updated_at;

}