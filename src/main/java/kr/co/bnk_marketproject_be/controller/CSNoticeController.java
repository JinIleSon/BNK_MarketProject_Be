package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import kr.co.bnk_marketproject_be.service.CSNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CSNoticeController {

    private final CSNoticeService csNoticeService;

    /* 공지사항 목록 */
    @GetMapping("cs/notice/list")
    public String noticeList(@RequestParam(required = false) String boardType,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {

        log.info("📢 boardType={}, page={}", boardType, page);

        int pageSize = 10; // 페이지당 게시글 수
        List<CSNoticeDTO> dtoList = csNoticeService.getNoticeList(page, boardType);
        int totalCount = csNoticeService.getTotalCount(boardType);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 모든 모델 데이터 세팅
        model.addAttribute("dtoPage", dtoList != null ? dtoList : new ArrayList<>());
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("boardType", boardType);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "customer_service/notice/notice_list";
    }


    /* 공지사항 상세 보기(view) */
    @GetMapping("cs/notice/view/{id}")
    public String noticeView(
            @PathVariable Long id,
            @RequestParam(required = false) String boardType,
            Model model) {

        CSNoticeDTO dto = csNoticeService.getNoticeDetail(id);
        model.addAttribute("notice", dto);
        model.addAttribute("boardType", boardType);
        return "customer_service/notice/notice_view";
    }


}
