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
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String boardType2,
            @RequestParam(required = false) String boardType3,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            Model model) {

        List<CSNoticeDTO> qnaList = qnaService.getQnaList(userid, boardType2, boardType3, offset, limit);
        int totalCount = qnaService.getTotalCount(userid, boardType2, boardType3);
        int currentPage = offset / limit;
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        model.addAttribute("qnaList", qnaList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("boardType2", boardType2);
        model.addAttribute("boardType3", boardType3);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("limit", limit);
        model.addAttribute("userid", userid);

        return "customer_service/qna/qna_list";
    }

    /* QnA 상세 */
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        CSNoticeDTO qna = qnaService.getQnaview(id);

        model.addAttribute("qna", qna);
        model.addAttribute("boardType2", qna.getBoardType2());

        return "customer_service/qna/qna_view";
    }

    /* QnA 등록 */
    @PostMapping("/write")
    public String write(@ModelAttribute CSNoticeDTO qna) {
        qnaService.insertQna(qna);
        return "redirect:/qna/list";
    }

    
}
