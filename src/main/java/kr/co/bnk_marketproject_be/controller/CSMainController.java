package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminAnnouncementDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminInquiryDTO;
import kr.co.bnk_marketproject_be.service.AdminAnnouncementService;
import kr.co.bnk_marketproject_be.service.AdminInquiryNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CSMainController {

    private final AdminAnnouncementService adminAnnouncementService;
    private final AdminInquiryNoticeService  adminInquiryNoticeService;

    @GetMapping("/cs/index")
    public String index(Model model, PageRequestDTO pageRequestDTO) {

        PageResponseAdminAnnouncementDTO pageResponseAdminAnnouncementDTO = adminAnnouncementService.selectAllAdminAnnouncement(pageRequestDTO);

        log.info("pageResponseAdminAnnouncementDTO={}", pageResponseAdminAnnouncementDTO);
        model.addAttribute("pageResponseAdminAnnouncementDTO", pageResponseAdminAnnouncementDTO);

        PageResponseAdminInquiryDTO pageResponseAdminInquiryDTO = adminInquiryNoticeService.selectAllAdminInquiry(pageRequestDTO);

        log.info("pageResponseAdminInquiryDTO={}", pageResponseAdminInquiryDTO);
        model.addAttribute("pageResponseAdminInquiryDTO", pageResponseAdminInquiryDTO);

        return "customer_service/cs_main";
    }


}
