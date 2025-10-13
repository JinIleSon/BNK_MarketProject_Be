package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminBannerDTO;
import kr.co.bnk_marketproject_be.service.AdminBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminBannerController {

    private final AdminBannerService bannerService;

    // ==============================
    // ① HTML 페이지 렌더링 (관리자 화면)
    // ==============================
    @GetMapping("/admin/admin_banner")
    public String adminBannerPage() {
        return "admin/admin_banner"; // templates/admin/admin_banner.html
    }

    // ==============================
    // ② REST API - 배너 목록 조회
    // ==============================
    @GetMapping("/api/admin/banners")
    @ResponseBody
    public ResponseEntity<List<AdminBannerDTO>> getBanners(
            @RequestParam(required = false) String position) {

        List<AdminBannerDTO> list = bannerService.findAll(position);
        return ResponseEntity.ok(list);
    }

    // ==============================
    // ③ REST API - 배너 등록
    // ==============================
    @PostMapping("/api/admin/banners")
    @ResponseBody
    public ResponseEntity<String> registerBanner(@ModelAttribute AdminBannerDTO dto,
                                                 @RequestParam(required = false) MultipartFile file) {
        bannerService.save(dto, file);
        return ResponseEntity.ok("등록 성공");
    }

    // ==============================
    // ④ REST API - 배너 삭제
    // ==============================
    @DeleteMapping("/api/admin/banners")
    @ResponseBody
    public ResponseEntity<Void> deleteBanners(@RequestBody List<Long> ids) {
        bannerService.deleteAll(ids);
        return ResponseEntity.ok().build();
    }
}
