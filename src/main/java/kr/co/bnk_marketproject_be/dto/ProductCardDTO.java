package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// main 페이지 이미지 매핑용
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCardDTO {
    private Long id;
    private Integer price;
    private Integer discount;
    private Integer hits;
    private Integer sold;
    private String createdAt;   // to_char로 받아옴
    private String url;         // "/images/xxx.jpg"
    private Double ratingAvg;
    private Integer ratingCnt;
}
