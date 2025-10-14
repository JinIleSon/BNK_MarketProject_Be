package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.mapper.ProductsMapper;
import kr.co.bnk_marketproject_be.service.OrdersService;
import kr.co.bnk_marketproject_be.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductsMapper productsMapper; // 또는 ProductService
    private final ProductService productService;
    private final OrdersService ordersService;

    private String currentUserId(Authentication auth) {
        // 예: username 이 users.id 이거나 이메일이면 서비스에서 변환
        return auth != null ? auth.getName() : null;
    }

    // ================================
    // 테스트용 uid 훅 (운영 전 제거!)
    // ================================
    private int resolveUid(Authentication auth, Integer uidParam) {
        if (uidParam != null) return uidParam;              // TODO(운영전 제거): 쿼리스트링 uid 강제 주입 허용
        String userId = currentUserId(auth);
        if (userId != null && userId.matches("\\d+")) {
            return Integer.parseInt(userId);
        }
        return 1; // TODO(운영전 제거): 인증 없을 때 임시 기본 사용자 ID
    }

    /* 상품 목록*/
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

    /* 상품 상세보기*/
    @GetMapping("/product/views")
    public String product_views(@RequestParam int id,
                                @RequestParam(defaultValue="1") int rpg,
                                @RequestParam(defaultValue="5") int rsize,
                                Model model){
        ProductViewsDTO dto = productService.getProductDetail(id);
        PageRequestProductDTO req = PageRequestProductDTO.builder().pg(rpg).size(rsize).build();
        PageResponseProductDTO<ProductBoardsDTO> reviewPage =
                productService.getProductReviewPage(id, req); // 페이징된 리뷰

        model.addAttribute("product", dto);
        model.addAttribute("reviewPage", reviewPage);
        return "product/product_views";
    }

    /* 장바구니 */
    @GetMapping("/product/cart")
    public String product_cart(
            Authentication auth,
            @RequestParam(name = "uid", required = false) Integer uidParam, // TODO(운영전 제거)
            Model model
    ) {
        int uid = resolveUid(auth, uidParam); // TODO(운영전 제거): 테스트 훅
        ProductCartDTO cart = ordersService.getCart(uid);
        model.addAttribute("cart", cart);
        return "product/product_cart";
    }

    /* 장바구니 담기 */
    @PostMapping(value = "/product/cart", produces = "application/json")
    @ResponseBody
    public Map<String,Object> addToCartAjax(Authentication auth,
                                            @RequestParam int productId,
                                            @RequestParam(required=false) Integer optionId,
                                            @RequestParam(defaultValue="1") int qty,
                                            @RequestParam(required=false) Integer uid) {

        int uidResolved = resolveUid(auth, uid);
        ordersService.addToCart(uidResolved, productId, optionId, qty);
        ProductCartDTO cart = ordersService.getCart(uidResolved);
        int count = (cart != null && cart.getItems()!=null) ? cart.getItems().size() : 0;
        return Map.of("ok", true, "itemCount", count, "summary", cart != null ? cart.getSummary() : null);
    }

    /* 구매하기 버튼(장바구니 담고 주문하기 페이지로 이동) */
    @PostMapping("/product/cart/add-and-go")
    public String addToCartAndGo(Authentication auth,
                                 @RequestParam int productId,
                                 @RequestParam(required=false) Integer optionId,
                                 @RequestParam(defaultValue="1") int qty,
                                 @RequestParam(required=false) Integer uid,
                                 RedirectAttributes ra) {

        int uidResolved = resolveUid(auth, uid);
        ordersService.addToCart(uidResolved, productId, optionId, qty);
        if (uid != null) ra.addAttribute("uid", uid); // 테스트 훅 유지
        return "redirect:/product/cart";
    }

    /* 주문정보 가져오기 */
    @GetMapping("/product/order")
    public String product_order(
            Authentication auth,
            @RequestParam(name = "uid", required = false) Integer uidParam, // TODO(운영전 제거)
            Model model
    ) {
        int uid = resolveUid(auth, uidParam); // TODO(운영전 제거): 테스트 훅
        ProductOrderDTO order = ordersService.getOrderPage(uid);
        model.addAttribute("order", order);
        return "product/product_order";
    }

    /* 주문하기 전송 */
    @PostMapping("/product/order")
    public String submit_order(
            Authentication auth,
            @ModelAttribute OrderPageSubmitDTO submit,
            RedirectAttributes ra
    ) {
        String userId = currentUserId(auth);
        if (userId == null || !userId.matches("\\d+")) {
            // TODO: 운영 전 제거 - 임시 체크
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        int uid = Integer.parseInt(userId);

        int orderId = ordersService.checkout(uid, submit);     // ✅ int 반환
        ra.addAttribute("orderId", orderId);                   // ✅ orderId로 전달
        return "redirect:/product/complete";
    }


    /* 주문 완료 ex) /product/complete?orderId=number */
    @GetMapping("/product/complete")
    public String product_complete(
            @RequestParam int orderId,
            Model model
    ) {
        ProductCompleteDTO complete = ordersService.getComplete(orderId); // 로그인 검사 제거
        model.addAttribute("complete", complete);
        return "product/product_complete";
    }

    /* 상품 검색*/
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
