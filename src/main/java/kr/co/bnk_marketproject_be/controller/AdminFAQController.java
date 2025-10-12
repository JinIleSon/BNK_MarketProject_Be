package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminFAQDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminFAQDTO;
import kr.co.bnk_marketproject_be.service.AdminFAQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminFAQController {

    private final AdminFAQService adminFAQService;

    @GetMapping("/admin/FAQ/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseAdminFAQDTO pageResponseAdminFAQDTO = adminFAQService.selectAllAdminFAQ(pageRequestDTO);

        log.info("pageResponseAdminFAQDTO={}", pageResponseAdminFAQDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminFAQDTO);

        return "admin/admin_questions_list";
    }

    @GetMapping("/admin/FAQ/search")
    public String searchList(Model model, PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO={}", pageRequestDTO);

        PageResponseAdminFAQDTO pageResponseAdminFAQDTO = adminFAQService.selectAllAdminFAQ(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminFAQDTO);

        return "admin/admin_questions_searchList";
    }

    // view 파일 출력
    @GetMapping("/admin/FAQ/view")
    public String view(int id, Model model) {
        log.info("id={}", id);
        adminFAQService.increaseHits(id);
        AdminFAQDTO adminFAQDTO = adminFAQService.getAdminFAQ(id);
        model.addAttribute("adminFAQDTO", adminFAQDTO);
        return "admin/admin_questions_view";
    }

    // list - [ 삭제 ] 및 view - 삭제 버튼 활성화
    @GetMapping("/admin/FAQ/delete")
    public String delete(int id) {
        log.info("id={}", id);
        adminFAQService.deleteAdminFAQ(id);

        return "redirect:/admin/FAQ/list";
    }

    @GetMapping("/admin/FAQ/write")
    public String writeList(Model model) {
        return "admin/admin_questions_write";
    }

    @PostMapping("/admin/FAQ/write")
    public String write(AdminFAQDTO adminFAQDTO) {
        log.info("adminFAQDTO={}", adminFAQDTO);

        adminFAQService.register(adminFAQDTO);
        return "redirect:/admin/FAQ/list";
    }

    // modify - list - [ 수정 ] 및 view - 수정 버튼 활성화
    @GetMapping("/admin/FAQ/modify/list")
    public String modifyList(int id, Model model) {
        log.info("id={}", id);

        AdminFAQDTO adminFAQDTO = adminFAQService.getAdminFAQ(id);
        model.addAttribute("adminFAQDTO", adminFAQDTO);
        return "admin/admin_questions_correction";
    }

    // modify 실행
    @PostMapping("/admin/FAQ/modify/list")
    public String modify(AdminFAQDTO adminFAQDTO, Model model) {
        log.info("adminFAQDTO={}", adminFAQDTO);

        adminFAQService.modifyAdminFAQ(adminFAQDTO);
        model.addAttribute("adminFAQDTO", adminFAQDTO);
        return "admin/admin_questions_view";
    }

    // 선택삭제
    @PostMapping("/admin/FAQ/multi-delete")
    @ResponseBody
    public String deleteFAQs(@RequestBody List<Integer> ids){
        log.info("ids={}", ids);
        adminFAQService.deleteFAQs(ids);
        return "OK";
    }
}
