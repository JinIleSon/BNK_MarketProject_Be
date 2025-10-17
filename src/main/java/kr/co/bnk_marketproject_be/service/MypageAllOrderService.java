package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.*;
import java.util.List;

public interface MypageAllOrderService {

    int findUserIdByUsername(String username);
    List<OrdersDTO> getAllOrdersByUserId(String userId);
    OrdersDTO getOrderDetail(int ordersId);
    PageResponseMypageAllOrderDTO getPagedOrders(PageRequestDTO pageRequestDTO, String userId);
    void insertProductBoard(ProductBoardsDTO dto);

    // ✅ 추가
    List<OrdersDTO> findAllOrdersByUserId(String userId);
}
