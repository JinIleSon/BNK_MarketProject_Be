package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.PageRequestProductDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseProductDTO;
import kr.co.bnk_marketproject_be.dto.ProductViewsDTO;
import kr.co.bnk_marketproject_be.dto.ProductsDTO;
import kr.co.bnk_marketproject_be.mapper.ProductsMapper;
import kr.co.bnk_marketproject_be.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductsMapper productsMapper; // 또는 ProductService
    private final ProductService productService;

    @GetMapping("/product/list")
    public String productList(@RequestParam(defaultValue = "1") int pg,
                              @RequestParam(defaultValue = "8") int size,
                              @RequestParam(defaultValue = "recent") String sort,
                              Model model) {

        PageRequestProductDTO pageReq = PageRequestProductDTO.builder()
                .pg(pg)
                .size(size)
                .build();

        // 1️⃣ 상품 목록 조회
        List<ProductsDTO> products = productsMapper.selectProductListPaged(pageReq, sort);
        int total = productsMapper.selectTotalProductCount();

        for (ProductsDTO p : products) {
            log.info("상품명: {}, 이미지URL: {}", p.getProduct_name(), p.getUrl());
        }

        PageResponseProductDTO<ProductsDTO> pageRes =
                new PageResponseProductDTO<>(pageReq, products, total); // 기존 쓰시던 응답 DTO

        model.addAttribute("pageResponseProductDTO", pageRes);
        model.addAttribute("sort", sort); // 정렬 탭 활성화용
        return "product/product_list";
    }

    @GetMapping("/product/views")
    public String product_views(@RequestParam int id, Model model){
        var dto = productService.getProductDetail(id);
        System.out.println("product dto = " + dto); // 반드시 확인
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found: " + id);
        }
        model.addAttribute("product", dto);
        return "product/product_views";
    }

    @GetMapping("/product/cart")
    public String product_cart(){ return "product/product_cart"; }

    @GetMapping("/product/order")
    public String product_order(){ return "product/product_order"; }

    @GetMapping("/product/complete")
    public String product_complete(){ return "product/product_complete"; }

    @GetMapping("/product/search")
    public String productSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "recent") String sort,
            @RequestParam(defaultValue = "1") int pg,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        PageRequestProductDTO req = PageRequestProductDTO.builder()
                .keyword(keyword)
                .searchType(searchType)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .sort(sort)
                .pg(pg)
                .size(size)
                .build();

        var list  = productsMapper.selectProductSearch(req);
        int total = productsMapper.selectProductSearchTotal(req);

        var page = new PageResponseProductDTO<>(req, list, total);

        model.addAttribute("pageResponseProductDTO", page);
        model.addAttribute("sort", sort);
        model.addAttribute("query", req); // 뷰에서 기존 값 유지용
        return "product/product_search";
    }

}
