package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.OrdersDTO;
import kr.co.bnk_marketproject_be.service.MypageAllOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MypageAllOrderController {

    // 주문 전체 내역 가져오기
    private final MypageAllOrderService orderService;

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

}
