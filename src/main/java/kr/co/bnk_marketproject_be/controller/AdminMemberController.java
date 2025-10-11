package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminMemberDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminMemberDTO;
import kr.co.bnk_marketproject_be.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping("/admin/member/list")
    public String adminStoreList(Model model, PageRequestDTO pageRequestDTO) {

        PageResponseAdminMemberDTO pageResponseDTO = adminMemberService.findAdminMemberAll(pageRequestDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/admin_member_list";
    }


    @GetMapping("/admin/member/search")
    public String adminStoreSearch(PageRequestDTO pageRequestDTO, Model model){

        log.info("pageRequestDTO:{}",pageRequestDTO);

        PageResponseAdminMemberDTO pageResponseDTO = adminMemberService.findAdminMemberAll(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/admin_member_searchList";
    }

//    @GetMapping("/admin/onemember/detail")
//    public AdminMemberDTO selectMember(String user_id) {
//        log.info("조회한 user_id: {}", user_id);
//        return adminMemberService.selectMember(user_id);
//    }

    @GetMapping(value = "/admin/onemember/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AdminMemberDTO> selectMember(@RequestParam("user_id") String userId) {
        log.info("조회한 user_id: {}", userId);
        AdminMemberDTO dto = adminMemberService.selectMember(userId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto); // JSON 직렬화
    }

    @PostMapping("/admin/onemember/detail")
    public String modifyMember(AdminMemberDTO adminMemberDTO) {
        log.info("adminMemberDTO:{}",adminMemberDTO);

        adminMemberService.modifyMember(adminMemberDTO);
        return "redirect:/admin/member/list";
    }

    @PostMapping("/admin/onemember/update")
    public String updateMember(@RequestParam("userId") String userId) {
        adminMemberService.updateOneMember(userId);
        return "redirect:/admin/member/list";
    }
}