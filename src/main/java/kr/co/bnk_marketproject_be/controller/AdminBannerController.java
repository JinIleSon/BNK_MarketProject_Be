package kr.co.bnk_marketproject_be.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminBannerController {

    @GetMapping("/admin_banner")
    public String adminBanner() {
        // templates/admin/admin_banner.html 로 렌더링
        return "admin/admin_banner";
    }
}
