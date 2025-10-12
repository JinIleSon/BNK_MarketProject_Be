package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminEmployDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminEmployDTO;
import kr.co.bnk_marketproject_be.service.AdminEmployService;
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
public class AdminEmployController {

    private final AdminEmployService adminEmployService;

    @GetMapping("/admin/employ/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseAdminEmployDTO pageResponseAdminEmployDTO = adminEmployService.selectAdminEmployAll(pageRequestDTO);

        log.info("pageResponseAdminEmployDTO={}", pageResponseAdminEmployDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminEmployDTO);

        return "admin/admin_hiring";
    }

    @GetMapping("/admin/employ/search")
    public String searchList(Model model, PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO={}", pageRequestDTO);

        PageResponseAdminEmployDTO pageResponseAdminEmployDTO = adminEmployService.selectAdminEmployAll(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseAdminEmployDTO);

        return "admin/admin_hiring_searchList";
    }

    @GetMapping("/admin/employ/delete")
    public String delete(int id) {
        adminEmployService.delete(id);
        return "redirect:/admin/employ/list";
    }
    
    @PostMapping("/admin/employ/register")
    public String register(AdminEmployDTO adminEmployDTO) {
        log.info("adminEmployDTO={}", adminEmployDTO);

        adminEmployDTO.setStatus("모집중");
        adminEmployDTO.setUser_name("최고관리자");
        adminEmployService.registerEmploy(adminEmployDTO);
        return "redirect:/admin/employ/list";
    }

    @PostMapping("/admin/employ/multi-delete")
    @ResponseBody
    public String deleteMulti(@RequestBody List<Integer> ids) {
        log.info("ids={}", ids);
        adminEmployService.deleteEmploys(ids);
        return "OK";
    }
}
