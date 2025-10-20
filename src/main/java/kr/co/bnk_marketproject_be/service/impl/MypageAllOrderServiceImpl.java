package kr.co.bnk_marketproject_be.service.impl;

import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.mapper.MypageAllOrderMapper;
import kr.co.bnk_marketproject_be.service.MypageAllOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageAllOrderServiceImpl implements MypageAllOrderService {

    private final MypageAllOrderMapper orderMapper;

    @Override
    public int findUserIdByUsername(String username) {
        return orderMapper.findUserIdByUsername(username);
    }

    @Override
    public List<OrdersDTO> getAllOrdersByUserId(String userId) {
        return this.findAllOrdersByUserId(userId);

    }

    @Override
    public OrdersDTO getOrderDetail(int ordersId) {
        return orderMapper.findOrderDetailById(ordersId);
    }

    @Override
    public PageResponseMypageAllOrderDTO getPagedOrders(PageRequestDTO pageRequestDTO, String userId) {
        List<OrdersDTO> list = orderMapper.findPagedOrders(userId, pageRequestDTO);
        int total = orderMapper.countOrdersByUserId(userId);

        return PageResponseMypageAllOrderDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total(total)
                .build();
    }

    @Override
    public void insertProductBoard(ProductBoardsDTO dto) {
        orderMapper.insertProductBoard(dto);
    }

    // ✅ 상품평 버튼용 전체 주문 + 상품목록 세팅
    @Override
    public List<OrdersDTO> findAllOrdersByUserId(String userId) {
        List<OrdersDTO> orders = orderMapper.findAllOrdersByUserId(userId);



        // ✅ 여기 아래 추가!
        for (OrdersDTO order : orders) {
            for (OrderItemsDTO item : order.getOrderItems()) {
                System.out.println("[DEBUG] orderId=" + order.getId() + ", imageUrl=" + item.getUrl());
            }
        }

        return orders;
    }
}
