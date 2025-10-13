package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminOpsStatsDTO;
import kr.co.bnk_marketproject_be.service.AdminInquiryService;
import kr.co.bnk_marketproject_be.service.AdminNoticeService;
import kr.co.bnk_marketproject_be.service.AdminStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminMainController {

    private final AdminNoticeService adminNoticeService;
    private final AdminInquiryService adminInquiryService;
    private final AdminStatsService adminStatsService;

    /** 관리자 메인 페이지 (뷰 렌더링) */
    @GetMapping("/admin/admin_main")
    public String list(Model model) {
        model.addAttribute("notices", adminNoticeService.getLatest(5));
        model.addAttribute("inquiries", adminInquiryService.latest(5));
        return "admin/admin_main";
    }

    /** 운영 통계 API (JSON 응답) */
    @GetMapping("/api/admin/ops-stats")
    @ResponseBody
    public AdminOpsStatsDTO opsStats(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        LocalDateTime startAt = (from == null) ? LocalDate.now().atStartOfDay() : from;
        LocalDateTime endAt   = (to == null)   ? LocalDate.now().plusDays(1).atStartOfDay() : to;

        return adminStatsService.getOpsStats(startAt, endAt);
    }
}
