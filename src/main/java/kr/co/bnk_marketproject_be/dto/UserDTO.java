package kr.co.bnk_marketproject_be.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class UserDTO {

    private int id;
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

    //@Builder.Default // 기본 초기화, 추후계획예정
    private String role = "user";

    private String created_at;
    private String updated_at;

}