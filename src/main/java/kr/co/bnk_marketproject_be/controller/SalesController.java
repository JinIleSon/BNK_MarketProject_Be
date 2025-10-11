package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseSalesDTO;
import kr.co.bnk_marketproject_be.service.SalesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SalesController {
    private final SalesService salesService;

    @GetMapping("/admin/sales/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseSalesDTO pageResponseSalesDTO = salesService.selectSalesAll(pageRequestDTO);

        log.info("pageResponseSalesDTO={}", pageResponseSalesDTO);
        model.addAttribute("pageResponseDTO", pageResponseSalesDTO);

        return "admin/admin_sale_status";
    }

}
