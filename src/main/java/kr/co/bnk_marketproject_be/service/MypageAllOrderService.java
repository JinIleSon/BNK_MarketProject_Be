package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.OrdersDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseMypageAllOrderDTO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface MypageAllOrderService {

    List<OrdersDTO> getAllOrdersByUserId(String userId);
    OrdersDTO getOrderDetail(int ordersId);
    int findUserIdByUsername(String username);
    PageResponseMypageAllOrderDTO getPagedOrders(PageRequestDTO pageRequestDTO, String userId);

}
