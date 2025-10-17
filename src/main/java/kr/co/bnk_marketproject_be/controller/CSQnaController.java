package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import kr.co.bnk_marketproject_be.service.CSQnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cs/qna")
@Slf4j
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
    public String view(
            @PathVariable Long id,
            @RequestParam(required = false) String boardType2,
            @RequestParam(required = false) String boardType3,
            Model model,
            CSNoticeDTO dto) {

        CSNoticeDTO qna = qnaService.getQnaview(id);
        qna.setLook(dto.getLook());
        log.info("qna={}", qna);
        CSNoticeDTO comment = qnaService.selectCommentView(id.intValue());
        log.info("comment={}", comment);

        model.addAttribute("qna", qna);
        model.addAttribute("boardType2", boardType2 != null ? boardType2 : qna.getBoardType2());
        model.addAttribute("boardType3", boardType3);
        model.addAttribute("comment", comment);

        return "customer_service/qna/qna_view";
    }

    /* QnA Mapping */
    @GetMapping("/write")
    public String write(
            @RequestParam(required = false) String boardType2,
            @RequestParam(required = false) String boardType3,
            Model model) {

        model.addAttribute("qna", new CSNoticeDTO());
        model.addAttribute("boardType2", boardType2);
        model.addAttribute("boardType3", boardType3);

        return "customer_service/qna/qna_write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute CSNoticeDTO qna) {

        qnaService.insertQna(qna);

        return "redirect:/cs/qna/list";
    }
}
