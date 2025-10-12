package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.service.AdminInquiryNoticeService;
import kr.co.bnk_marketproject_be.service.AdminInquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminInquiryController {

    private final AdminInquiryNoticeService adminInquiryService;

    @GetMapping("/admin/inquiry/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseAdminInquiryDTO pageResponseAdminInquiryDTO = adminInquiryService.selectAllAdminInquiry(pageRequestDTO);

        log.info("pageResponseAdminInquiryDTO={}", pageResponseAdminInquiryDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminInquiryDTO);

        return "admin/admin_asking";
    }

    @GetMapping("/admin/inquiry/search")
    public String searchList(Model model, PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO={}", pageRequestDTO);

        PageResponseAdminInquiryDTO pageResponseAdminInquiryDTO = adminInquiryService.selectAllAdminInquiry(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminInquiryDTO);

        return "admin/admin_asking_searchList";
    }

    // list - [ 삭제 ] 및 view - 삭제 버튼 활성화
    @GetMapping("/admin/inquiry/delete")
    public String delete(Long id) {
        log.info("id={}", id);
        adminInquiryService.deleteAdminInquiry(id);

        return "redirect:/admin/inquiry/list";
    }


    // view + insert - 문의하기 답변 페이지
    @GetMapping("/admin/inquiry/answer")
    public String answer(Long id, Model model) {
        log.info("id={}", id);

        adminInquiryService.increaseHits(id);
        AdminInquiryDTO adminInquiryDTO = adminInquiryService.getAdminInquiry(id);
        model.addAttribute("adminInquiryDTO", adminInquiryDTO);
        return "admin/admin_asking_answer";
    }

    // view + insert - 실행
    @PostMapping("/admin/inquiry/answer")
    public String answer(AdminCommentDTO adminCommentDTO, Long bid) {
        log.info("adminCommentDTO={}", adminCommentDTO);
        log.info("id={}", bid);

        adminInquiryService.registerInquiryComment(adminCommentDTO);
        return "redirect:/admin/inquiry/view?id=" + bid;
    }

    // view + comment 출력
    @GetMapping("/admin/inquiry/view")
    public String view(@RequestParam("id") Long id, Model model) {
        log.info("id={}", id);
        adminInquiryService.increaseHits(id);
        AdminInquiryDTO adminInquiryDTO = adminInquiryService.getAdminInquiry(id);
        AdminCommentDTO adminCommentDTO = adminInquiryService.getAdminComment(id);
        model.addAttribute("adminInquiryDTO", adminInquiryDTO);
        model.addAttribute("adminCommentDTO", adminCommentDTO);
        return "admin/admin_asking_detail";
    }

    // 선택삭제
    @PostMapping("/admin/inquiry/multi-delete")
    @ResponseBody
    public String deleteMulti(@RequestBody List<Long> ids) {
        log.info("ids={}", ids);
        adminInquiryService.deleteInquirys(ids);
        return "OK";
    }
}
