package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminProductDTO;
import kr.co.bnk_marketproject_be.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminProductController {

    private final AdminProductService adminMemberService;

    @GetMapping("/admin/product/list")
    public String adminStoreList(Model model, PageRequestDTO pageRequestDTO) {

        PageResponseAdminProductDTO pageResponseDTO = adminMemberService.findAdminProductAll(pageRequestDTO);

        log.info("pageResponseDTO={}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/admin_productList";
    }

    @GetMapping("/admin/product/search")
    public String adminStoreSearch(PageRequestDTO pageRequestDTO, Model model){

        log.info("pageRequestDTO:{}",pageRequestDTO);

        PageResponseAdminProductDTO pageResponseDTO = adminMemberService.findAdminProductAll(pageRequestDTO);

        log.info("pageResponseDTO={}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/admin_product_searchList";
    }

    @PostMapping("/admin/product/multi-delete")
    @ResponseBody
    public String deleteProducts(@RequestBody List<Integer> ids) {
        log.info("ids:{}",ids);

        adminMemberService.deleteProducts(ids);
        return "OK";
    }
}