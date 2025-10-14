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

    /** 개발 모드: URL의 uid가 있으면 그걸 최우선 사용, 없으면 세션(Authentication), 둘 다 없으면 401 */
    private int resolveUid(Authentication auth, Integer uidParam) {
        if (uidParam != null) return uidParam;                        // ← ★ url이 최우선
        if (auth != null && auth.isAuthenticated()) {
            String name = auth.getName();
            if (name != null && name.matches("\\d+")) return Integer.parseInt(name);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "개발모드: uid 필요");
    }

    /* 상품 목록 */
    @GetMapping("/product/list")
    public String productList(@RequestParam(defaultValue = "1") int pg,
                              @RequestParam(defaultValue = "8") int size,
                              @RequestParam(defaultValue = "recent") String sort,
                              @RequestParam(required = false) Integer categoryId, // ← 손진일 - 추가
                              @RequestParam(required = false) Integer uid,   // 🔹 URL uid 유지용
                              Model model) {

        PageRequestProductDTO pageReq = PageRequestProductDTO.builder()
                .pg(pg)
                .size(size)
                .categoryId(categoryId)
                .build();

        // 1️⃣ 상품 목록 조회
        List<ProductsDTO> products = productsMapper.selectProductListPaged(pageReq, sort, categoryId);
        int total = productsMapper.selectTotalProductCount(categoryId); // ← 손진일 - 전체 개수도 필터 적용

        for (ProductsDTO p : products) {
            log.info("[LIST] 상품명: {}, 이미지URL: {}", p.getProduct_name(), p.getUrl());
        }

        PageResponseProductDTO<ProductsDTO> pageRes =
                new PageResponseProductDTO<>(pageReq, products, total);

        // 🔹 뷰에서 링크 만들 때 그대로 붙이도록 전달
        model.addAttribute("pageResponseProductDTO", pageRes);
        model.addAttribute("sort", sort);
        model.addAttribute("categoryId", categoryId); // ← 손진일 - 페이지네이션/링크 유지
        model.addAttribute("uid", uid); // ← 중요: pagination/상세보기 링크에 함께 넘겨라

        // ★ 여기 추가: 리스트 화면에서도 query를 항상 제공
        model.addAttribute("query", PageRequestProductDTO.builder()
                .categoryId(categoryId)   // 카테고리 유지되게
                .build());

        return "product/product_list";
    }

    /* 상품 상세보기 */
    @GetMapping("/product/views")
    public String product_views(@RequestParam int id,
                                @RequestParam(defaultValue = "1") int rpg,
                                @RequestParam(defaultValue = "5") int rsize,
                                @RequestParam(required = false) Integer uid,  // 🔸 URL로 받는 uid
                                Model model) {

        // 1) 상품 상세
        ProductViewsDTO dto = productService.getProductDetail(id);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다.");
        }

        // 2) 리뷰 페이지
        PageRequestProductDTO req = PageRequestProductDTO.builder()
                .pg(rpg)
                .size(rsize)
                .build();
        PageResponseProductDTO<ProductBoardsDTO> reviewPage =
                productService.getProductReviewPage(id, req);

        // 3) 모델 적재 (뷰에서 링크에 uid 계속 붙이도록)
        model.addAttribute("product", dto);
        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("uid", uid); // 🔸 중요: 이후 링크(th:href/onclick)에서 사용

        // (선택) 디버깅 로그
        log.info("[VIEWS] id={}, rpg={}, rsize={}, uid={}", id, rpg, rsize, uid);

        return "product/product_views";
    }

//    /* 장바구니 */
//    @GetMapping("/product/cart")
//    public String product_cart(
//            Authentication auth,
//            @RequestParam(name = "uid", required = false) Integer uidParam, // TODO(운영전 제거)
//            Model model
//    ) {
//        int uid = currentUid(auth); // TODO(운영전 제거): 테스트 훅
//        ProductCartDTO cart = ordersService.getCart(uid);
//        model.addAttribute("cart", cart);
//        return "product/product_cart";
//    }

    @GetMapping("/product/cart")
    public String product_cart(Authentication auth,
                               @RequestParam(required = false) Integer uid, // ← URL로 받을 수 있게
                               Model model) {

        int resolvedUid = resolveUid(auth, uid);
        ProductCartDTO cart = ordersService.getCart(resolvedUid);

        model.addAttribute("cart", cart);
        model.addAttribute("uid", resolvedUid); // ← 뷰에서 다음 링크에 계속 붙일 수 있게

        log.info("[CART] uid={}", resolvedUid);
        return "product/product_cart";
    }

    /* 장바구니 담기 */
    @PostMapping(value = "/product/cart", produces = "application/json")
    @ResponseBody
    public Map<String, Object> addToCartAjax(
            Authentication auth,
            @RequestParam int productId,
            @RequestParam(required = false) Integer optionId,
            @RequestParam(defaultValue = "1") int qty,
            @RequestParam(required = false) Integer uid // ← URL에서 받기(선택)
    ) {
        int resolvedUid = resolveUid(auth, uid); // ← URL ?uid= 우선, 없으면 세션
        log.info("[ADD_TO_CART] uid={}, productId={}, optionId={}, qty={}", resolvedUid, productId, optionId, qty);

        try {
            // 실제 담기
            ordersService.addToCart(resolvedUid, productId, optionId, qty);

            // 담은 뒤 카운트 갱신용 조회
            ProductCartDTO cart = ordersService.getCart(resolvedUid);
            int count = (cart != null && cart.getItems() != null) ? cart.getItems().size() : 0;

            return Map.of(
                    "ok", true,
                    "uid", resolvedUid,
                    "itemCount", count,
                    "summary", cart != null ? cart.getSummary() : null
            );
        } catch (IllegalArgumentException e) {
            log.warn("[ADD_TO_CART][VALIDATION] {}", e.getMessage());
            return Map.of("ok", false, "reason", e.getMessage());
        } catch (Exception e) {
            log.error("[ADD_TO_CART][ERROR]", e);
            return Map.of("ok", false, "reason", "SERVER_ERROR");
        }
    }

    /* 구매하기 버튼(장바구니 담고 주문하기 페이지로 이동) */
    @PostMapping("/product/cart/add-and-go")
    public String addToCartAndGo(
            Authentication auth,
            @RequestParam int productId,
            @RequestParam(required = false) Integer optionId,
            @RequestParam(defaultValue = "1") int qty,
            @RequestParam(required = false) Integer uid,
            RedirectAttributes ra
    ) {
        int resolvedUid = resolveUid(auth, uid); // ← URL ?uid= 우선, 없으면 세션에서

        try {
            ordersService.addToCart(resolvedUid, productId, optionId, qty);
            // 장바구니 화면에서도 같은 uid 유지
            ra.addAttribute("uid", resolvedUid);
            return "redirect:/product/cart";
        } catch (IllegalArgumentException e) {
            // 유효성 문제(재고 부족/옵션 불일치 등)
            ra.addFlashAttribute("error", e.getMessage());
            ra.addAttribute("uid", resolvedUid);
            return "redirect:/product/views?id=" + productId; // 원래 상품 상세로 복귀
        } catch (Exception e) {
            // 기타 서버 오류
            ra.addFlashAttribute("error", "SERVER_ERROR");
            ra.addAttribute("uid", resolvedUid);
            return "redirect:/product/views?id=" + productId;
        }
    }

    /* 주문정보 가져오기 */
    @GetMapping("/product/order")
    public String product_order(
            Authentication auth,
            @RequestParam(required = false) Integer uid,   // URL ?uid= 우선
            Model model,
            RedirectAttributes ra
    ) {
        int resolvedUid = resolveUid(auth, uid);

        ProductOrderDTO order = ordersService.getOrderPage(resolvedUid);

        // ✅ items 기준으로 비어있는지 체크
        if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
            ra.addFlashAttribute("info", "장바구니에 담긴 상품이 없습니다.");
            ra.addAttribute("uid", resolvedUid);   // uid 유지
            return "redirect:/product/cart";
        }

        model.addAttribute("order", order);
        model.addAttribute("uid", resolvedUid);    // 뷰에서 계속 사용
        return "product/product_order";
    }

    /* 주문하기 전송 */
    @PostMapping("/product/order")
    public String submit_order(
            Authentication auth,
            @RequestParam(required = false) Integer uid,   // URL의 uid 우선 사용 (없으면 세션)
            @ModelAttribute OrderPageSubmitDTO submit,
            RedirectAttributes ra
    ) {
        int resolvedUid = resolveUid(auth, uid);

        try {
            int orderId = ordersService.checkout(resolvedUid, submit); // ✅ 주문 생성
            ra.addAttribute("orderId", orderId);                      // 완료화면으로 전달
            return "redirect:/product/complete";
        } catch (IllegalArgumentException e) {
            // 유효성 문제(재고 부족, 장바구니 비어있음 등) → 주문화면으로 복귀
            ra.addFlashAttribute("error", e.getMessage());
            ra.addAttribute("uid", resolvedUid); // uid 유지
            return "redirect:/product/order";
        } catch (Exception e) {
            // 기타 서버 오류
            ra.addFlashAttribute("error", "주문 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
            ra.addAttribute("uid", resolvedUid);
            return "redirect:/product/order";
        }
    }


    /* 주문 완료: /product/complete?orderId=123[&uid=1] */
    @GetMapping("/product/complete")
    public String product_complete(
            Authentication auth,
            @RequestParam int orderId,
            @RequestParam(required = false) Integer uid,   // URL의 uid 허용(없으면 세션/로그인)
            Model model,
            RedirectAttributes ra
    ) {
        int resolvedUid = resolveUid(auth, uid);

        try {
            // 주문 완료 헤더 + 라인들 조회 (서비스에서 상태가 '결제완료'인 것만 반환하도록 구현되어 있으면 best)
            ProductCompleteDTO complete = ordersService.getComplete(orderId);

            if (complete == null) {
                ra.addFlashAttribute("error", "해당 주문을 찾을 수 없거나 접근 권한이 없습니다.");
                ra.addAttribute("uid", resolvedUid);
                return "redirect:/product/order";
            }

            // (선택) 소유자 검증을 하고 싶다면 서비스에 검증 추가 후 사용:
            // if (!ordersService.isOrderOwnedBy(orderId, resolvedUid)) { ... }

            model.addAttribute("complete", complete);
            return "product/product_complete";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            ra.addAttribute("uid", resolvedUid);
            return "redirect:/product/order";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "주문 완료 정보를 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.");
            ra.addAttribute("uid", resolvedUid);
            return "redirect:/product/order";
        }
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
            @RequestParam(required = false) Integer categoryId, // 손진일 - 추가
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
        model.addAttribute("categoryId", categoryId);     // 손진일 - 추가
        return "product/product_search";
    }

}
