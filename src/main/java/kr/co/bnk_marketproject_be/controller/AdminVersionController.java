package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminVersionDTO;
import kr.co.bnk_marketproject_be.service.AdminVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminVersionController {

    private final AdminVersionService versionService;

    // ===== [1] 관리자 페이지 뷰 반환 =====
    @GetMapping("/admin/admin_version")
    public String adminVersionPage() {
        return "admin/admin_version"; // Thymeleaf 템플릿
    }

    // ===== [2] 전체 목록 조회 (JSON) =====
    @GetMapping("/api/admin/versions")
    @ResponseBody
    public List<AdminVersionDTO> list() {
        return versionService.list();
    }

    // ===== [3] 등록 (버전 추가) =====
    @PostMapping("/api/admin/versions")
    @ResponseBody
    public Map<String, Object> add(@RequestBody Map<String, String> body, Principal principal) {
        String versionName = body.getOrDefault("versionName", "").trim();
        String author = (principal != null)
                ? principal.getName()
                : body.getOrDefault("author", "관리자");
        String changeLog = body.getOrDefault("changeLog", "").trim();

        if (versionName.isEmpty()) {
            return Map.of("ok", false, "msg", "버전명이 비어 있습니다.");
        }

        AdminVersionDTO dto = AdminVersionDTO.builder()
                .versionName(versionName)
                .author(author)
                .changeLog(changeLog)
                .build();

        int result = versionService.add(dto);
        log.info("✅ 버전 등록: {} by {}", versionName, author);
        return Map.of("ok", result > 0);
    }

    // ===== [4] 단건 삭제 =====
    @DeleteMapping("/api/admin/versions/{id}")
    @ResponseBody
    public Map<String, Object> deleteOne(@PathVariable Long id) {
        int result = versionService.delete(id);
        return Map.of("ok", result > 0, "deletedId", id);
    }

    // ===== [5] 여러 개 삭제 (체크박스용) =====
    @DeleteMapping("/api/admin/versions")
    @ResponseBody
    public Map<String, Object> deleteMany(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        versionService.delete(ids);
        return Map.of("ok", true, "deleted", ids == null ? 0 : ids.size());
    }
}
