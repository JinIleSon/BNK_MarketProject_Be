package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.Coupons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponsRepository extends JpaRepository<Coupons, Integer> {
    @Query("SELECT MAX(c.code) FROM Coupons c")
    String findMaxCode();
}
