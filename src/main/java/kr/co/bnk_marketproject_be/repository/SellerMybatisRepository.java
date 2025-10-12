package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.dto.SellerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.List;

@Mapper
public interface SellerMybatisRepository {

    // 판매자 등록
    void insertSeller(SellerDTO seller);

    // ID 중복 체크
    //int countBySellerId(@Param("sellerId") String sellerId);
    int countBySellerId(String sellerId);

    default boolean existsBySellerId(String sellerId) {
        return countBySellerId(sellerId) > 0;
    }

    // 이메일 중복 체크
    int countByEmail(@Param("email") String email);

    // 전화번호 중복 체크
    int countByPhone(@Param("phone") String phone);

    // 이름 + 이메일 or 전화번호로 ID 찾기
    SellerDTO findByNameAndEmail(@Param("name") String name, @Param("email") String email);
    SellerDTO findByNameAndPhone(@Param("name") String name, @Param("phone") String phone);

    // sellerId + email/phone으로 비밀번호 찾기 인증
    SellerDTO findBySellerIdAndEmail(@Param("sellerId") String sellerId, @Param("email") String email);
    SellerDTO findBySellerIdAndPhone(@Param("sellerId") String sellerId, @Param("phone") String phone);

    // 비밀번호 변경
    void updatePassword(@Param("sellerId") String sellerId, @Param("password") String password);
}
