package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminSiteConfigDTO;
import kr.co.bnk_marketproject_be.service.AdminSiteConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminVersionController {

    private final AdminSiteConfigService service;

    // ===== 뷰 반환 (페이지)
    @GetMapping("/admin/admin_version")
    public String adminVersion() {
        return "admin/admin_version";
    }

    // ===== API (JSON) - 경로만 다르게!
    @GetMapping("/api/admin/versions")
    @ResponseBody
    public List<AdminSiteConfigDTO> list() {
        return service.list();
    }

    @PutMapping("/api/admin/versions")
    @ResponseBody
    public Map<String, Object> update(@RequestBody Map<String, String> body, Principal principal) {
        String version   = body.getOrDefault("version", "").trim();
        String changeLog = body.getOrDefault("changeLog", "").trim();
        String author    = principal != null ? principal.getName() : body.getOrDefault("author", "SYSTEM");

        AdminSiteConfigDTO dto = AdminSiteConfigDTO.builder()
                .version(version)
                .changeLog(changeLog)
                .author(author)
                .build();

        service.update(dto);
        return Map.of("ok", true);
    }

    @DeleteMapping("/api/admin/versions")
    @ResponseBody
    public Map<String, Object> delete(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        service.delete(ids);
        return Map.of("ok", true, "deleted", ids == null ? 0 : ids.size());
    }
}
