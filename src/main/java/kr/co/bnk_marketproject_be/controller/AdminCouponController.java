package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.CouponsDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminCouponDTO;
import kr.co.bnk_marketproject_be.service.AdminCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminCouponController {

    private final AdminCouponService adminCouponService;

    @GetMapping("/admin/coupon/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseAdminCouponDTO pageResponseAdminCouponDTO = adminCouponService.selectCouponsAll(pageRequestDTO);

        log.info("pageResponseAdminCouponDTO={}", pageResponseAdminCouponDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminCouponDTO);

        return "admin/admin_coupon";
    }

    @GetMapping("/admin/coupon/search")
    public String searchList(Model model, PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO={}", pageRequestDTO);

        PageResponseAdminCouponDTO pageResponseAdminCouponDTO = adminCouponService.selectCouponsAll(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminCouponDTO);

        return "admin/admin_coupon_searchList";
    }

    @PostMapping("/admin/coupon/register")
    public String register(CouponsDTO couponsDTO) {
        log.info("couponsDTO={}", couponsDTO);

        adminCouponService.register(couponsDTO);
        return "redirect:/admin/coupon/list";
    }
}
