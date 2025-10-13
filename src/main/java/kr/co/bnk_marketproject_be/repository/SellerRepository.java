package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<User, String> {

    // 사용자 정의 쿼리배서드
    //public int countBySellerId(String sellerId);
    //public int countByBrand_name(String brand_name);
    //public int countByBiz_registration_number(String biz_registration_number);
    //public int countByMail_order_number(String mail_order_number);


//    private int id;
//
//    private int userId;
//    private String sellerId;
//    private String brand_name;
//    private String biz_registration_number;
//    private String mail_order_number;
}