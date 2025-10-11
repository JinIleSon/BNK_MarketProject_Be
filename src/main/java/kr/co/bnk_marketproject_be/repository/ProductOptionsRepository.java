package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.ProductOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionsRepository extends JpaRepository<ProductOptions, Integer> {
}
