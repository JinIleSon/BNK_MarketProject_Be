package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, Integer> {
    // products_id의 최대값
    @Query("select max(pi.products_id) from ProductImages pi")
    Integer findMaxProductsId();
}
