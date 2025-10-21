package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.*;
import java.util.List;

public interface MypageAllOrderService {

    int findUserIdByUsername(String username);

    List<OrdersDTO> getAllOrdersByUserId(String userId);

    List<OrdersDTO> findRecentOrdersByUserId(String userId);

    // ✅ 주문 상세 (주문코드 기반)
    List<OrdersDTO> getOrderDetailByCode(String orderCode);

    PageResponseMypageAllOrderDTO getPagedOrders(PageRequestDTO pageRequestDTO, String userId);

    void insertProductBoard(ProductBoardsDTO dto);

    List<OrdersDTO> findAllOrdersByUserId(String userId);
}
