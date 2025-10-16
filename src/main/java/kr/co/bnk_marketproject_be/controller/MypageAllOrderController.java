package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.OrdersDTO;
import kr.co.bnk_marketproject_be.dto.ProductBoardsDTO;
import kr.co.bnk_marketproject_be.mapper.MypageAllOrderMapper;
import kr.co.bnk_marketproject_be.service.MypageAllOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MypageAllOrderController {

    // 주문 전체 내역 가져오기
    private final MypageAllOrderService orderService;
    private final MypageAllOrderMapper orderMapper;

    @GetMapping("/mypage/mypage/allorder")
    public String MypageAllOrder(Model model, Principal principal) {
        System.out.println("🔥 [Controller] >>> /mypage/orderall 호출됨");

        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        System.out.println("✅ [Controller] 로그인 아이디: " + username);

        int userId = orderService.findUserIdByUsername(username);
        System.out.println("✅ [Controller] 조회된 userId: " + userId);

        List<OrdersDTO> orders = orderService.getAllOrdersByUserId(String.valueOf(userId));
        System.out.println("✅ [Controller] 불러온 주문 개수: " + (orders != null ? orders.size() : 0));

        model.addAttribute("orders", orders);

        return "mypage/mypage_allOrder";
    }

    @PostMapping("/api/mypage/review")
    @ResponseBody
    public ResponseEntity<String> insertReview(
            @RequestParam("productId") int productId,
            @RequestParam("rating") int rating,
            @RequestParam("body") String body,
            Principal principal) {

        // ✅ 로그 추가 (요청이 제대로 들어오는지 확인)
        log.info("🧩 [insertReview] productId={}, rating={}, body={}", productId, rating, body);

        // 로그인한 유저 아이디 찾기
        int usersId = orderService.findUserIdByUsername(principal.getName());

        // DTO 구성
        ProductBoardsDTO dto = ProductBoardsDTO.builder()
                .products_id(productId)
                .users_id(usersId)
                .type("REVIEW")        // 상품평 타입 구분
                .title("상품평")         // 제목 고정
                .content(body)          // 작성 내용
                .rating(rating)         // 별점
                .build();

        // DB insert
        int result = orderMapper.insertProductBoard(dto);
        // ✅
        log.info("🧾 [insertReview] DB insert 결과: {}", result);

        // 정상 등록 여부에 따라 응답 반환
        if (result > 0) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }
    }


}
