package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminCouponsNowDTO;
import kr.co.bnk_marketproject_be.service.AdminCouponsNowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminCouponsNowController {

    private final AdminCouponsNowService adminCouponsNowService;

    @GetMapping("/admin/couponsnow/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseAdminCouponsNowDTO pageResponseAdminCouponsNowDTO = adminCouponsNowService.selectCouponsNowAll(pageRequestDTO);

        log.info("pageResponseAdminCouponsNowDTO={}", pageResponseAdminCouponsNowDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminCouponsNowDTO);

        return "admin/admin_couponIssue";
    }

    @GetMapping("/admin/couponsnow/search")
    public String searchList(Model model, PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO={}", pageRequestDTO);

        PageResponseAdminCouponsNowDTO pageResponseAdminCouponsNowDTO = adminCouponsNowService.selectCouponsNowAll(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminCouponsNowDTO);

        return "admin/admin_couponIssue_searchList";
    }
}
