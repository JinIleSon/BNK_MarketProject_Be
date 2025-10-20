package kr.co.bnk_marketproject_be.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.bnk_marketproject_be.dto.AdminSiteConfigDTO;
import kr.co.bnk_marketproject_be.service.AdminSiteConfigService;
import kr.co.bnk_marketproject_be.service.AdminStorageService;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.env.Environment;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminBasicController {

    private final AdminSiteConfigService siteConfigService;
    private final Environment environment;
    private final AdminStorageService storageService;

    //기본설정
    // ===== GET =====
    @GetMapping("/admin/admin_basic")
    public String adminBasic(Model model, HttpServletRequest request) {

        request.getSession();

        AdminSiteConfigDTO config = siteConfigService.get();
        if (config == null) config = new AdminSiteConfigDTO();  // 프리필 비어도 에러는 안 나게

        Path staticRoot  = Paths.get("src/main/resources/static");
        Path imagesDir   = staticRoot.resolve("images");
        Path logoPath    = imagesDir.resolve("logo.png");
        Path faviconPath = staticRoot.resolve("favicon.ico");

        // 실제 파일이 있을 때만 경로 세팅
        config.setHeaderLogo(Files.exists(logoPath)    ? "/images/logo.png" : null);
        config.setFavicon(   Files.exists(faviconPath) ? "/favicon.ico"     : null);

        model.addAttribute("config", config);
        model.addAttribute("appVersion",
                environment.getProperty("spring.application.version", "unknown"));
        return "admin/admin_basic";
    }

    // ===== POST =====
    @PostMapping("/admin/admin_basic")
    public String updateAdminBasic(
            @ModelAttribute("config") AdminSiteConfigDTO form,
            @RequestParam(required = false) MultipartFile headerLogoFile,
            @RequestParam(required = false) MultipartFile footerLogoFile, // 사용 안 함(그대로 유지)
            @RequestParam(required = false) MultipartFile faviconFile,
            @RequestParam(required = false) String removeHeaderLogo,
            @RequestParam(required = false) String removeFooterLogo, // 사용 안 함(그대로 유지)
            @RequestParam(required = false) String removeFavicon,
            RedirectAttributes ra) {

        log.info("POST /admin/admin_basic form = {}", form);

        // 텍스트 필드 병합
        AdminSiteConfigDTO current = siteConfigService.get();
        if (current == null) current = new AdminSiteConfigDTO();

        form.setSiteTitle(  isBlank(form.getSiteTitle())      ? current.getSiteTitle()   : form.getSiteTitle());
        form.setSiteSubtitle(isBlank(form.getSiteSubtitle())  ? current.getSiteSubtitle(): form.getSiteSubtitle());
        form.setVersion(    isBlank(form.getVersion())        ? current.getVersion()     : form.getVersion());

        // === 고정 저장 경로 ===
        Path staticRoot  = Paths.get("src/main/resources/static");
        Path imagesDir   = staticRoot.resolve("images");
        Path logoPath    = imagesDir.resolve("logo.png");     // 항상 이 파일명
        Path faviconPath = staticRoot.resolve("favicon.ico"); // 항상 이 파일명

        try {
            Files.createDirectories(imagesDir);

            // ── 로고 (항상 PNG, 파일명 고정: images/logo.png) ──
            if (removeHeaderLogo != null) {
                Files.deleteIfExists(logoPath);
                // 화면은 고정 경로로 불러오므로 DB null이어도 무방하지만 일관성 위해 null
                form.setHeaderLogo(null);
            } else if (headerLogoFile != null && !headerLogoFile.isEmpty()) {
                // PNG가 아니어도 PNG로 변환해서 저장
                try (InputStream in = headerLogoFile.getInputStream()) {
                    BufferedImage img = ImageIO.read(in);
                    if (img == null) {
                        ra.addFlashAttribute("msg", "로고 파일을 읽을 수 없습니다. PNG/JPG 등 이미지 파일을 업로드해주세요.");
                        return "redirect:/admin/admin_basic";
                    }
                    // 무조건 PNG로 저장
                    ImageIO.write(img, "png", logoPath.toFile());
                }
                // 뷰/DB엔 고정 URL
                form.setHeaderLogo("/images/logo.png");
            } else {
                // 업로드 없으면 경로 고정
                form.setHeaderLogo("/images/logo.png");
            }

            // ── 파비콘 (항상 ICO, 파일명 고정: /favicon.ico) ──
            if (removeFavicon != null) {
                Files.deleteIfExists(faviconPath);
                form.setFavicon(null);
            } else if (faviconFile != null && !faviconFile.isEmpty()) {
                String originalName = faviconFile.getOriginalFilename();
                boolean looksIco = originalName != null && originalName.toLowerCase().endsWith(".ico");
                String ct = faviconFile.getContentType() != null ? faviconFile.getContentType() : "";
                boolean mimeIco = ct.equalsIgnoreCase("image/x-icon") || ct.equalsIgnoreCase("image/vnd.microsoft.icon");

                if (!(looksIco || mimeIco)) {
                    ra.addFlashAttribute("msg", "파비콘은 .ico 파일만 가능합니다. (예: favicon.ico)");
                    return "redirect:/admin/admin_basic";
                }

                // ★ 덮어쓰기 확정 방식 1: 삭제 후 스트림 복사
                try (InputStream in = faviconFile.getInputStream()) {
                    Files.deleteIfExists(faviconPath);         // 기존 파일 제거
                    Files.copy(in, faviconPath);               // 새 파일 복사
                }

                // 덮어쓰기 확정 방식 2 (대안): 바이트 쓰기
                // Files.write(faviconPath, faviconFile.getBytes(),
                //        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

                form.setFavicon("/favicon.ico");
            } else {
                form.setFavicon("/favicon.ico");
            }

            // footerLogo는 그대로 유지(요청 미대상)
            form.setFooterLogo(current.getFooterLogo());

        } catch (Exception e) {
            log.error("File upload error", e);
            ra.addFlashAttribute("msg", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/admin/admin_basic";
        }

        // 저장
        int updated = siteConfigService.upsert(form);
        log.info("updated rows = {}", updated);

        ra.addFlashAttribute("msg", "저장되었습니다.");
        return "redirect:/admin/admin_basic";
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }
}
