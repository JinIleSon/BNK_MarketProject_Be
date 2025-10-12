package kr.co.bnk_marketproject_be.service;

import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.ProductOptionsDTO;
import kr.co.bnk_marketproject_be.dto.ProductRegDTO;
import kr.co.bnk_marketproject_be.entity.ProductImages;
import kr.co.bnk_marketproject_be.entity.ProductOptions;
import kr.co.bnk_marketproject_be.entity.ProductReg;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import kr.co.bnk_marketproject_be.repository.ProductImagesRepository;
import kr.co.bnk_marketproject_be.repository.ProductOptionsRepository;
import kr.co.bnk_marketproject_be.repository.ProductRegRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsRegService {
    private final ProductRegRepository productRegRepository;
    private final ProductOptionsRepository productOptionsRepository;
    private final ProductImagesRepository productImagesRepository;
    private final ModelMapper modelMapper;
    private final AdminMapper adminMapper;

    @Transactional
    public void save(ProductRegDTO productRegDTO) {

        productRegDTO.setStatus("active");
        // 1) select option에서 받아온 2자리 코드 (예: 11, 22, 31 ...)
        int second = productRegDTO.getSecond();

        // 2) second에 따라 sellers_id, categories_id 설정
        int sellers_id = 0;
        int categories_id = 0;

        if (second == 11) { sellers_id = 1; categories_id = 2; }
        else if (second == 12) { sellers_id = 2; categories_id = 3; }
        else if (second == 13) { sellers_id = 3; categories_id = 4; }
        else if (second == 14) { sellers_id = 4; categories_id = 5; }
        else if (second == 21) { sellers_id = 5; categories_id = 7; }
        else if (second == 22) { sellers_id = 6; categories_id = 8; }
        else if (second == 31) { sellers_id = 7; categories_id = 10; }
        else if (second == 32) { sellers_id = 8; categories_id = 11; }
        else if (second == 41) { sellers_id = 9; categories_id = 13; }
        else if (second == 42) { sellers_id = 10; categories_id = 14; }
        else if (second == 51) { sellers_id = 11; categories_id = 16; }
        else if (second == 52) { sellers_id = 12; categories_id = 17; }
        else {
            throw new IllegalArgumentException("지원하지 않는 코드입니다: " + second);
        }

        // 3) 해당 구간(예: 2200~2299)에서 가장 큰 product_code 찾기
        int start = second * 100;
        int end = second * 100 + 99;

        Integer max_product_code = productRegRepository.findMaxProductCodeInRange(start, end);

        // 👉 가장 큰 값 + 1 로 새 코드 생성 (없으면 첫 값으로 시작)
        int new_product_code = (max_product_code != null) ? Math.min(max_product_code + 1, end) : start + 1;

        // 4) DTO에 값 세팅
        productRegDTO.setSellers_id(sellers_id);
        productRegDTO.setCategories_id(categories_id);
        productRegDTO.setProduct_code(new_product_code);

        // 엔티티 변환 및 저장
        ProductReg entity = modelMapper.map(productRegDTO, ProductReg.class);
        ProductReg saved = productRegRepository.save(entity);

        // 추가) product_options 테이블에 option_name, product_code 매핑
        String option_name = productRegDTO.getOption_name();

        ProductOptionsDTO productOptionsDTO = new ProductOptionsDTO();
        productOptionsDTO.setOption_name(option_name);
        productOptionsDTO.setProduct_code(productRegDTO.getProduct_code());
        productOptionsRepository.save(modelMapper.map(productOptionsDTO, ProductOptions.class));

        // 추가) product_images 테이블에 products_id(products_id 중 최대값으로 매핑), url 매핑
        String url = productRegDTO.getUrl();

        int productsId  = saved.getId();
        int productCode = saved.getProduct_code();

        if (url != null && !url.isBlank()){

            // products_id = 현재 최대값 + 1
            Integer maxProductsId = productImagesRepository.findMaxProductsId();
            int nextProductsId = (maxProductsId == null) ? 1 : maxProductsId + 1;
            ProductImages productImages = ProductImages.builder()
                    .products_id(productsId)
                    .product_code(productCode)
                    .url(url)
                    .is_main("Y")
                    .created_at(null)
                    .build();
            productImagesRepository.save(productImages);
            log.info("product_images 저장 완료: products_id={}, url={}", nextProductsId, url);
        } else {
            log.info("이미지 URL이 없어 product_images 저장을 건너뜀");
        }
            log.info("상품 저장 완료 → second={}, product_code={}, sellers_id={}, categories_id={}",
                second, new_product_code, sellers_id, categories_id);

    }
    public void deleteProduct(int product_code){
        adminMapper.deleteProduct(product_code);
    };
}
