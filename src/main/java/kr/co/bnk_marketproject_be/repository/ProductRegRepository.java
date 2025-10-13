package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.ProductReg;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRegRepository extends JpaRepository<ProductReg, Integer> {

    @Query("select max(p.product_code) from ProductReg p " +
            "where p.product_code between :start and :end")
    Integer findMaxProductCodeInRange(@Param("start") int start,
                                      @Param("end") int end);
}
