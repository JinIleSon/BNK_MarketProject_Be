package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminBannerDTO;
import kr.co.bnk_marketproject_be.service.AdminBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminBannerController {

    private final AdminBannerService service;

    /** 페이지: templates/admin/admin_banner.html 렌더링 */
    @GetMapping("/admin/admin_banner")
    public String adminBannerPage() {
        return "admin/admin_banner";
    }

    /** 목록: /api/admin/banners?position=MAIN1 */
    @GetMapping("/api/admin/banners")
    @ResponseBody
    public List<AdminBannerDTO> listByPosition(
            @RequestParam(name = "position", defaultValue = "MAIN1") String position) {
        return service.listByPosition(position);
    }

    /** 등록: multipart/form-data */
    @PostMapping(value = "/api/admin/banners", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public AdminBannerDTO create(@ModelAttribute AdminBannerDTO form) {
        return service.create(form);
    }

    /** 선택삭제: JSON 배열 [1,2,3] */
    @DeleteMapping("/api/admin/banners")
    @ResponseBody
    public void bulkDelete(@RequestBody List<Long> ids) {
        service.deleteByIds(ids);
    }

    /** 상태 토글 */
    @PatchMapping("/api/admin/banners/{id}/status")
    @ResponseBody
    public AdminBannerDTO toggle(@PathVariable Long id, @RequestParam boolean enable) {
        return service.toggleStatus(id, enable);
    }
}
