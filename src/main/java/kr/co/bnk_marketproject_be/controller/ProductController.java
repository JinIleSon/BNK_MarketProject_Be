package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.mapper.ProductsMapper;
import kr.co.bnk_marketproject_be.security.MyUserDetails;
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

    private final ProductsMapper productsMapper;
    private final ProductService productService;
    private final OrdersService ordersService;

    /** 현재 로그인 사용자의 DB PK(id) 반환. 미로그인 시 401 */
    private int currentUserIdOr401(Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");

        Object p = auth.getPrincipal();
        if (p instanceof MyUserDetails mud && mud.getUser() != null) {
            Integer id = mud.getUser().getId();
            if (id != null) return id;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "세션에서 사용자 정보를 찾지 못했습니다.");
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

        PageResponseProductDTO<ProductsDTO> pageRes =
                new PageResponseProductDTO<>(pageReq, products, total);

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
                                Model model) {

        ProductViewsDTO dto = productService.getProductDetail(id);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다.");
        }

        PageRequestProductDTO req = PageRequestProductDTO.builder()
                .pg(rpg).size(rsize).build();
        PageResponseProductDTO<ProductBoardsDTO> reviewPage =
                productService.getProductReviewPage(id, req);

        model.addAttribute("product", dto);
        model.addAttribute("reviewPage", reviewPage);
        return "product/product_views";
    }

    /* 장바구니 */
    @GetMapping("/product/cart")
    public String product_cart(Authentication auth, Model model) {
        int uid = currentUserIdOr401(auth);
        ProductCartDTO cart = ordersService.getCart(uid);
        model.addAttribute("cart", cart);
        return "product/product_cart";
    }

    /* 장바구니 담기 (AJAX) */
    @PostMapping(value = "/product/cart", produces = "application/json")
    @ResponseBody
    public Map<String, Object> addToCartAjax(Authentication auth,
                                             @RequestParam int productId,
                                             @RequestParam(required = false) Integer optionId,
                                             @RequestParam(defaultValue = "1") int qty) {
        int uid = currentUserIdOr401(auth);
        log.info("[ADD_TO_CART] uid={}, productId={}, optionId={}, qty={}", uid, productId, optionId, qty);

        try {
            ordersService.addToCart(uid, productId, optionId, qty);

            ProductCartDTO cart = ordersService.getCart(uid);
            int count = (cart != null && cart.getItems() != null) ? cart.getItems().size() : 0;

            return Map.of("ok", true, "itemCount", count, "summary", cart != null ? cart.getSummary() : null);
        } catch (IllegalArgumentException e) {
            log.warn("[ADD_TO_CART][VALIDATION] {}", e.getMessage());
            return Map.of("ok", false, "reason", e.getMessage());
        } catch (Exception e) {
            log.error("[ADD_TO_CART][ERROR]", e);
            return Map.of("ok", false, "reason", "SERVER_ERROR");
        }
    }

//    @PostMapping("/product/cart")
//    public

    /* 구매하기 버튼(담고 장바구니 페이지로 이동) */
    @PostMapping("/product/cart/add-and-go")
    public String addToCartAndGo(Authentication auth,
                                 @RequestParam int productId,
                                 @RequestParam(required = false) Integer optionId,
                                 @RequestParam(defaultValue = "1") int qty,
                                 RedirectAttributes ra) {
        int uid = currentUserIdOr401(auth);
        try {
            ordersService.addToCart(uid, productId, optionId, qty);
            return "redirect:/product/cart";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/product/views?id=" + productId;
        } catch (Exception e) {
            ra.addFlashAttribute("error", "SERVER_ERROR");
            return "redirect:/product/views?id=" + productId;
        }
    }

    /* 주문정보 가져오기 */
    @GetMapping("/product/order")
    public String product_order(Authentication auth, Model model, RedirectAttributes ra) {
        int uid = currentUserIdOr401(auth);

        ProductOrderDTO order = ordersService.getOrderPage(uid);
        if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
            ra.addFlashAttribute("info", "장바구니에 담긴 상품이 없습니다.");
            return "redirect:/product/cart";
        }

        model.addAttribute("order", order);
        return "product/product_order";
    }

    /* 주문하기 전송 */
    @PostMapping("/product/order")
    public String submit_order(Authentication auth,
                               @ModelAttribute OrderPageSubmitDTO submit,
                               RedirectAttributes ra) {
        int uid = currentUserIdOr401(auth);
        try {
            int orderId = ordersService.checkout(uid, submit);
            ra.addAttribute("orderId", orderId);
            return "redirect:/product/complete";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/product/order";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "주문 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
            return "redirect:/product/order";
        }
    }

    /* 주문 완료 */
    @GetMapping("/product/complete")
    public String product_complete(Authentication auth,
                                   @RequestParam int orderId,
                                   Model model,
                                   RedirectAttributes ra) {
        currentUserIdOr401(auth); // 소유자 검증 추가하려면 서비스에서 검사
        try {
            ProductCompleteDTO complete = ordersService.getComplete(orderId);
            if (complete == null) {
                ra.addFlashAttribute("error", "해당 주문을 찾을 수 없거나 접근 권한이 없습니다.");
                return "redirect:/product/order";
            }
            model.addAttribute("complete", complete);
            return "product/product_complete";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/product/order";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "주문 완료 정보를 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.");
            return "redirect:/product/order";
        }
    }

    /* 상품 검색*/
    @GetMapping("/product/header/search")
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

        // 실제 조회용: 미선택(null/blank)이면 name 으로 처리
        String effectiveSearchType = (searchType == null || searchType.isBlank()) ? "name" : searchType;

        // 가격 검색인 경우, keyword는 상품명 기준으로 사용하도록 설정
        // (mapper 쿼리에서 price + name 조건을 함께 사용)
        PageRequestProductDTO req = PageRequestProductDTO.builder()
                .keyword(keyword)
                .searchType(effectiveSearchType)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .sort(sort)
                .pg(pg)
                .size(size)
                .categoryId(categoryId)
                .build();

        // Mapper 호출 (통합 쿼리만 사용)
        List<ProductsDTO> list = productsMapper.selectProductSearch(req);
        int total = productsMapper.selectProductSearchTotal(req);

        var page = new PageResponseProductDTO<>(req, list, total);

        model.addAttribute("pageResponseProductDTO", page);
        model.addAttribute("sort", sort);
        // 화면 표시는 사용자가 넘긴 원래 값 그대로(null이면 null)
        model.addAttribute("query", PageRequestProductDTO.builder()
                .keyword(keyword)
                .searchType(searchType)       // ← null 그대로 넘김: 라디오 미선택으로 보임
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .sort(sort)
                .pg(pg)
                .size(size)
                .categoryId(categoryId)
                .build());
        model.addAttribute("categoryId", categoryId);
        return "product/product_search";
    }

}
