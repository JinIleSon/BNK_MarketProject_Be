package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminAnnouncementDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminAnnouncementDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminAnnouncementDTO;
import kr.co.bnk_marketproject_be.service.AdminAnnouncementService;
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
public class AdminAnnouncementController {

    private final AdminAnnouncementService adminAnnouncementService;

    @GetMapping("/admin/announcement/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseAdminAnnouncementDTO pageResponseAdminAnnouncementDTO = adminAnnouncementService.selectAllAdminAnnouncement(pageRequestDTO);

        log.info("pageResponseAdminAnnouncementDTO={}", pageResponseAdminAnnouncementDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminAnnouncementDTO);

        return "admin/admin_announce_list";
    }

    @GetMapping("/admin/announcement/search")
    public String searchList(Model model, PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO={}", pageRequestDTO);

        PageResponseAdminAnnouncementDTO pageResponseAdminAnnouncementDTO = adminAnnouncementService.selectAllAdminAnnouncement(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminAnnouncementDTO);

        return "admin/admin_announce_searchList";
    }

    // view 파일 출력
    @GetMapping("/admin/announcement/view")
    public String view(int id, Model model) {
        log.info("id={}", id);
        adminAnnouncementService.increaseHits(id);
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementService.getAdminAnnouncement(id);
        model.addAttribute("adminAnnouncementDTO", adminAnnouncementDTO);
        return "admin/admin_announce_view";
    }

    // list - [ 삭제 ] 및 view - 삭제 버튼 활성화
    @GetMapping("/admin/announcement/delete")
    public String delete(int id) {
        log.info("id={}", id);
        adminAnnouncementService.deleteAdminAnnouncement(id);

        return "redirect:/admin/announcement/list";
    }

    @GetMapping("/admin/announcement/write")
    public String writeList(Model model) {
        return "admin/admin_announce_write";
    }

    @PostMapping("/admin/announcement/write")
    public String write(AdminAnnouncementDTO adminAnnouncementDTO) {
        log.info("adminAnnouncementDTO={}", adminAnnouncementDTO);

        adminAnnouncementService.register(adminAnnouncementDTO);
        return "redirect:/admin/announcement/list";
    }

    // modify - list - [ 수정 ] 및 view - 수정 버튼 활성화
    @GetMapping("/admin/announcement/modify/list")
    public String modifyList(int id, Model model) {
        log.info("id={}", id);

        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementService.getAdminAnnouncement(id);
        model.addAttribute("adminAnnouncementDTO", adminAnnouncementDTO);
        return "admin/admin_announce_correction";
    }

    // modify 실행
    @PostMapping("/admin/announcement/modify/list")
    public String modify(AdminAnnouncementDTO adminAnnouncementDTO, Model model) {
        log.info("adminAnnouncementDTO={}", adminAnnouncementDTO);

        adminAnnouncementService.modifyAdminAnnouncement(adminAnnouncementDTO);
        model.addAttribute("adminAnnouncementDTO", adminAnnouncementDTO);
        return "admin/admin_announce_view";
    }

    // 선택삭제
    @PostMapping("/admin/announcement/multi-delete")
    @ResponseBody
    public String deleteAnnouncements(@RequestBody List<Integer> ids) {
        log.info("ids={}", ids);
        adminAnnouncementService.deleteAnnouncements(ids);
        return "OK";
    }

}
