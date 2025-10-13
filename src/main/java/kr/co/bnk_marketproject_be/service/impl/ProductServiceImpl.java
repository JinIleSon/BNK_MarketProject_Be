package kr.co.bnk_marketproject_be.service.impl;


import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.mapper.ProductsMapper;
import kr.co.bnk_marketproject_be.service.ProductService;
import lombok.RequiredArgsConstructor;
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
        // 1) 기본 상품만
        ProductViewsDTO dto = productsMapper.selectProductDetailBase(id);
        if (dto == null) return null;

        // 2) 이미지
        dto.setMainImage(productsMapper.selectMainImage(id));
        dto.setSubImages(productsMapper.selectProductDetailImages(id));

        // 3) 평점/리뷰
        Map<String, Object> agg = productsMapper.selectRatingAgg(id);
        if (agg != null) {
            dto.setRating_avg( agg.get("rating_avg") == null ? null : ((Number)agg.get("rating_avg")).doubleValue() );
            dto.setRating_cnt( ((Number)agg.getOrDefault("rating_cnt", 0)).intValue() );
        }
        dto.setReviews(productsMapper.selectProductReviews(id));

        // 4) 옵션
        dto.setOptions(productsMapper.selectProductOptions(id));

        // 5) 가격 계산(자바에서)
        int price    = dto.getPrice();
        int discount = dto.getDiscount();
        int sale     = Math.max(price - Math.max(discount, 0), 0);
        dto.setSale_price(sale);
        int rate = (price > 0 && discount > 0) ? (int)Math.round(100.0*discount/price) : 0;
        dto.setDiscount_rate(rate);
        dto.setFreeDelivery(dto.getDelichar() == 0);

        return dto;
    }
}

