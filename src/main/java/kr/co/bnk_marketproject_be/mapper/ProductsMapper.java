package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductsMapper {
    List<ProductsDTO> selectProductListPaged(@Param("page") PageRequestProductDTO page,
                                             @Param("sort") String sort,
                                             @Param("categoryId") Integer categoryId); // ← 손진일 - 추가
    int selectTotalProductCount(@Param("categoryId") Integer categoryId);             // ←  손진일 - 시그니처 변경

    List<ProductsDTO> selectProductSearch(@Param("req") PageRequestProductDTO req);
    int selectProductSearchTotal(@Param("req") PageRequestProductDTO req);

    // ====== 상세 조회(서비스 코드에 맞춤) ======
    /** 기본 상품 한 건(계산/이미지/평점 제외한 순수 행) */
    ProductViewsDTO selectProductDetailBase(@Param("id") int id);

    /** 대표 이미지 한 장 (is_main=1) */
    String selectMainImage(@Param("id") int id);

    /** 추가 이미지들 (is_main=0) */
    List<String> selectProductDetailImages(@Param("id") int id);

    /** 평점 평균/개수 집계 */
    Map<String, Object> selectRatingAgg(@Param("id") int id);

    /** 리뷰 목록 */
    List<ProductBoardsDTO> selectProductReviews(@Param("id") int id);

    /** 옵션 목록 */
    List<ProductOptionsDTO> selectProductOptions(@Param("id") int id);

    int countProductReviews(@Param("productId") int productId);

    List<ProductBoardsDTO> selectProductReviewsPaged(
            @Param("productId") int productId,
            @Param("req") PageRequestProductDTO req
    );

    ProductsDTO selectProductSeller(@Param("id") int id);

    int  insertCouponUser(@Param("user_id") String user_id);
}
