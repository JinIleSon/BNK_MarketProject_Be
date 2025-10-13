package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import kr.co.bnk_marketproject_be.service.CSFaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cs/faq")
public class CSFaqController {

    private final CSFaqService faqService;

    /* FAQ 목록 최초 3개 */
    @GetMapping("/list")
    public String faqList(@RequestParam(required = false) String boardType,
                          @RequestParam(required = false) String subType,
                          Model model) {

        if (boardType == null || boardType.isEmpty()) {
            boardType = "faq";
        }

        // ✅ 기존 서비스 그대로 사용 (Map 제거)
        List<CSNoticeDTO> faqList = faqService.getFaqListByType(boardType, 0, 100);

        // ✅ subType이 있으면 title 기준으로 필터링
        if (subType != null && !subType.isEmpty()) {
            faqList = faqList.stream()
                    .filter(faq -> faq.getTitle() != null && faq.getTitle().contains(subType))
                    .collect(Collectors.toList());
        }

        model.addAttribute("boardType", boardType);
        model.addAttribute("subType", subType);
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
