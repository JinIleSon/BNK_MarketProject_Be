package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestProductDTO {
    @Builder.Default private int pg = 1;
    @Builder.Default private int size = 8;

    // 🔹 검색/정렬용 필드 추가
    private String keyword;       // 검색어
    private String searchType;    // name | explain | price
    private Integer minPrice;     // 가격 최소 (옵션)
    private Integer maxPrice;     // 가격 최대 (옵션)
    @Builder.Default private String sort = "recent"; // sales | priceAsc | priceDesc | rating | review | recent

    // 손진일 - 추가
    @Builder.Default private Integer categoryId = 0;

    public int getOffset() { return (pg - 1) * size; }
}