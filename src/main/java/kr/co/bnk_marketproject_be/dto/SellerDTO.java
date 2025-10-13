package kr.co.bnk_marketproject_be.dto;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDTO {

    private int id;
    private int userId;
    private String sellerId;
    private String password;

    private String brand_name;
    private String biz_registration_number;
    private String mail_order_number;

    // user 테이블에서 가져오는 필드 추가, seller mybatis로 변경때문에 필요함
    private String name;
    private String email;
    private String phone;

    private String address;
    private String detailAddress;
    private String postcode;

    private String created_at;
    private String updated_at;


    // 비밀번호 암호화 setter (User와 구조 동일하게 유지)
    public void setPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}