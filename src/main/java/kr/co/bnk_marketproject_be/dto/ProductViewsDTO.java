package kr.co.bnk_marketproject_be.dto;

import kr.co.bnk_marketproject_be.entity.ProductBoards;
import kr.co.bnk_marketproject_be.entity.ProductOptions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductViewsDTO {
    // ────────────── 기본 상품 정보 ──────────────
    private int id;                 // PK
    private String product_code;     // 상품번호 (PRODUCTS.product_code)
    private String product_name;     // 상품명
    private String description;     // 상세 설명 (하단)
    private String sub_description;  // 간략 설명 (상단 요약)
    private String created_at;       // 등록일 (노출용)

    // ────────────── 가격 및 할인 정보 ──────────────
    private int price;              // 정상가
    private int discount;           // 할인액(원 단위)
    private int sale_price;          // 판매가(표시가) = price - discount
    private int discount_rate;       // 할인율 (%) = ROUND(100 * discount / price)
    private int delichar;           // 배송비 (0=무료)
    private boolean freeDelivery;   // 무료배송 여부 (delichar == 0)

    // ────────────── 이미지 정보 ──────────────
    private String mainImage;       // 대표 이미지 (is_main=1)
    private List<String> subImages; // 추가 이미지들 (is_main=0)

    // ────────────── 평점 / 리뷰 ──────────────
    private Double rating_avg;       // 평균 평점
    private int rating_cnt;          // 리뷰 수
    private List<ProductBoardsDTO> reviews; // 리뷰 목록

    // ────────────── 옵션 정보 ──────────────
    private List<ProductOptionsDTO> options; // 옵션 드롭다운용

    // ────────────── 판매자 / 추가 정보 ──────────────
    private int sellers_id;           // 판매자 (없으면 nullable)
    private String brand_name;      // 판매자명 (선택)

    // 계산 편의용 메서드 (선택)
    public void calculatePriceInfo() {
        if (price > 0 && discount > 0) {
            sale_price = price - discount;
            discount_rate = (int) Math.round((100.0 * discount) / price);
        } else {
            sale_price = price;
            discount_rate = 0;
        }
        freeDelivery = (delichar == 0);
    }

    // 뷰에서 쓰는 필드 추가 2
    private String make;
    private String origin;
    private String prodcondition;
}

