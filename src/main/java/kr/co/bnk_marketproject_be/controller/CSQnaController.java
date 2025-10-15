package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import kr.co.bnk_marketproject_be.service.CSQnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cs/qna")
public class CSQnaController {

    private final CSQnaService qnaService;

    /* QnA 목록 */
    /* QnA 목록 */
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String userid,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            Model model) {

        List<CSNoticeDTO> qnaList = qnaService.getQnaList(userid, offset, limit);
        model.addAttribute("qnaList", qnaList);

        return "customer_service/qna/qna_list";
    }

    /* QnA 상세 */
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        CSNoticeDTO qna = qnaService.getQnaview(id);

        model.addAttribute("qna", qna);

        return "customer_service/qna/qna_view";
    }

    /* QnA 등록 */
    @PostMapping("/write")
    public String write(@ModelAttribute CSNoticeDTO qna) {
        qnaService.insertQna(qna);
        return "redirect:/qna/list";
    }

}
