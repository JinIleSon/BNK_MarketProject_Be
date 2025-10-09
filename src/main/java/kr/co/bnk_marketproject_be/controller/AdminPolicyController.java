package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminSiteConfigDTO;
import kr.co.bnk_marketproject_be.mapper.AdminSiteConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminPolicyController {

    private final AdminSiteConfigMapper adminSiteConfigMapper;

    @GetMapping("/admin/admin_policy")
    public String adminPolicyPage(Model model) {
        AdminSiteConfigDTO config = adminSiteConfigMapper.selectOne();
        if (config == null) {
            config = new AdminSiteConfigDTO();
        }
        model.addAttribute("Config",config);
        return "admin/admin_policy";
    }

    /** 모달에서 '저장' 시 단일 약관만 갱신: type=BUYER|SELLER|FINANCE|LOCATION|PRIVACY */
    @PostMapping("/admin/terms")
    public String updateOneTerm(@RequestParam("type") String type,
                                @RequestParam("content") String content) {

        // 프론트와 동일 규칙(최대 500자) 방어
        if (content != null && content.length() > 500) {
            content = content.substring(0, 500);
        }

        AdminSiteConfigDTO patch = new AdminSiteConfigDTO();
        switch (type) {
            case "BUYER"     -> patch.setTermsBuyer(content);
            case "SELLER"    -> patch.setTermsSeller(content);
            case "FINANCE"   -> patch.setTermsFinance(content);
            case "LOCATION"  -> patch.setTermsLocation(content);
            case "PRIVACY"   -> patch.setTermsPrivacy(content);
            default -> {
                log.warn("Unknown terms type: {}", type);
                return "redirect:/admin/admin_policy";
            }
        }

        adminSiteConfigMapper.update(patch); // 부분 업데이트(<if> 조건들) 수행
        return "redirect:/admin/admin_policy";
    }

    /** 필요 시: 일괄 저장 (폼이 DTO 바인딩으로 올라오는 경우) */
    @PostMapping("/admin/terms/all")
    public String updateAllTerms(AdminSiteConfigDTO dto) {
        // 각 필드 500자 컷
        dto.setTermsBuyer(trim500(dto.getTermsBuyer()));
        dto.setTermsSeller(trim500(dto.getTermsSeller()));
        dto.setTermsFinance(trim500(dto.getTermsFinance()));
        dto.setTermsLocation(trim500(dto.getTermsLocation()));
        dto.setTermsPrivacy(trim500(dto.getTermsPrivacy()));

        adminSiteConfigMapper.update(dto);
        return "redirect:/admin/admin_policy";
    }

    private String trim500(String s) {
        if (s == null) return null;
        return s.length() > 500 ? s.substring(0, 500) : s;
    }
}
