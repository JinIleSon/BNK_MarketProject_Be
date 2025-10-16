package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
@ControllerAdvice(basePackages = "kr.co.bnk_marketproject_be.controller")
@RequiredArgsConstructor
public class BalanceModelAdviceController {

    private final PointService pointService;

    @ModelAttribute
    public void addBalance(Model model, Principal principal) {
        if (principal == null) return;            // 비로그인 예외
        String userId = principal.getName();      // 로그인 아이디
        long balance = pointService.getBalance(userId);
        model.addAttribute("balance", balance);   // 모든 mypage 뷰에서 사용 가능
    }
}
