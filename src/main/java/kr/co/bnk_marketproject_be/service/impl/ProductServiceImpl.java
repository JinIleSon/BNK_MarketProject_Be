package kr.co.bnk_marketproject_be.service.impl;


import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.mapper.ProductsMapper;
import kr.co.bnk_marketproject_be.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductsMapper productsMapper;

    @Override
    public ProductViewsDTO getProductDetail(int id) {
        // 1) 기본 상품(평점/개수 포함)
        ProductViewsDTO dto = productsMapper.selectProductDetailBase(id);
        if (dto == null) return null;

        // 2) 이미지
        dto.setMainImage(productsMapper.selectMainImage(id));
        dto.setSubImages(productsMapper.selectProductDetailImages(id));

        // 4) 옵션
        dto.setOptions(productsMapper.selectProductOptions(id));

        // 5) 자바 쪽 파생값 계산
        dto.calculatePriceInfo();                  // sale_price, discount_rate, freeDelivery 계산

        // (선택) rating_avg/rating_cnt 가 null 이면 fallback 집계쿼리
        if (dto.getRating_avg() == null || dto.getRating_cnt() == 0) {
            Map<String, Object> agg = productsMapper.selectRatingAgg(id);
            if (agg != null) {
                Object avg = agg.get("rating_avg");
                dto.setRating_avg(avg == null ? 0.0 : ((Number) avg).doubleValue());
                dto.setRating_cnt(((Number) agg.getOrDefault("rating_cnt", 0)).intValue());
            }
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseProductDTO<ProductBoardsDTO> getProductReviewPage(int productId,
                                                                         PageRequestProductDTO req) {
        // 총 개수
        var total = productsMapper.countProductReviews(productId);
        List<ProductBoardsDTO> list = total > 0
                ? productsMapper.selectProductReviewsPaged(productId, req)
                : List.of();

        // 페이지 응답 DTO 조립 (네가 쓰던 PageResponseProductDTO 그대로 활용)
        return new PageResponseProductDTO<>(req, list, total);
    }

    @Transactional
    public ProductsDTO selectProductSeller(int id){
        return  productsMapper.selectProductSeller(id);
    }

    @Transactional
    public int  insertCouponUser(String user_id){
        return productsMapper.insertCouponUser(user_id);
    }
}

