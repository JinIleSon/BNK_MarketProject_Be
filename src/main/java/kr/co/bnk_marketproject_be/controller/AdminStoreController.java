package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminStoreDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseDTO;
import kr.co.bnk_marketproject_be.service.AdminStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminStoreController {

    private final AdminStoreService adminStoreService;

    @GetMapping("/admin/shop/list")
    public String adminStoreList(Model model, PageRequestDTO pageRequestDTO) {

        PageResponseDTO pageResponseDTO = adminStoreService.findAdminStoreAll(pageRequestDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/admin_store_list";
    }

    @GetMapping("/admin/shop/search")
    public String adminStoreSearch(PageRequestDTO pageRequestDTO, Model model){

        log.info("pageRequestDTO:{}",pageRequestDTO);

        PageResponseDTO pageResponseDTO = adminStoreService.findAdminStoreAll(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/admin_store_searchList";
    }

    @PostMapping("/admin/shop/register")
    public String adminStoreRegister(AdminStoreDTO adminStoreDTO){
        log.info("adminStoreDTO:{}",adminStoreDTO);

        adminStoreDTO.setManage("운영 준비");
        adminStoreDTO.setLook("승인");
        adminStoreDTO.setBoardType("storelist");

        adminStoreService.registerStore(adminStoreDTO);
        return "redirect:/admin/shop/list";
    }
}