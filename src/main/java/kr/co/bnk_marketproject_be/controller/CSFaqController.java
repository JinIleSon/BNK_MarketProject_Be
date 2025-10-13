package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import kr.co.bnk_marketproject_be.service.CSFaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cs/faq")
public class CSFaqController {

    private final CSFaqService faqService;

    /* FAQ 목록 */
    @GetMapping("/list")
    public String list(Model model) {
        List<CSNoticeDTO> faqList = faqService.getFaqList();
        model.addAttribute("faqList", faqList);
        return "customer_service/faq/faq_list";
    }

    /* FAQ 상세 미구현 */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        CSNoticeDTO faq = faqService.getFaqDetail(id);
        model.addAttribute("faq", faq);
        return "customer_service/faq/faq_view";
    }

    /* 관리자 FAQ 등록 미구현 */
    @PostMapping("/write")
    public String write(@ModelAttribute CSNoticeDTO faq) {
        faqService.insertFaq(faq);
        return "redirect:/faq/list";
    }
}
