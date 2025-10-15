package kr.co.bnk_marketproject_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsDTO {

    private int id;
    private int product_code;
    private int sellers_id;
    private int categories_id;
    private String product_name;
    private String description;
    private int price;
    private int stock;
    private String status;
    private String created_at;
    private String updated_at;
    private int discount;
    private int hits;
    private int point;
    private String sub_description;
    private Integer delichar;

    // 뷰에서 쓰는 필드 추가
    private String url;        // 제품 이미지 URL
    private String user_name;  // 판매자 이름(qUser.name);
    private Double ratingAvg;   // 평균 별점 (null 가능)
    private Integer ratingCnt;  // 리뷰 수

}
