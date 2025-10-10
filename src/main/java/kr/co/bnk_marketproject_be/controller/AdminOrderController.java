package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.OrdersDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminOrderDTO;
import kr.co.bnk_marketproject_be.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping("/admin/order/list")
    public String adminOrderList(Model model, PageRequestDTO pageRequestDTO) {

        PageResponseAdminOrderDTO pageResponseDTO = adminOrderService.findAdminOrderAll(pageRequestDTO);

        log.info("pageResponseDTO={}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/admin_order";
    }

    @GetMapping("/admin/order/search")
    public String adminOrderSearch(PageRequestDTO pageRequestDTO, Model model){

        log.info("pageRequestDTO:{}",pageRequestDTO);

        PageResponseAdminOrderDTO pageResponseDTO = adminOrderService.findAdminOrderAll(pageRequestDTO);

        log.info("pageResponseDTO={}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/admin_order_searchList";
    }

    @GetMapping("/admin/order/detail")
    @ResponseBody
    public List<OrdersDTO> orderDetail(@RequestParam("order_code") String order_code){
        return adminOrderService.selectOrders(order_code);
    }
}