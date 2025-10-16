package kr.co.bnk_marketproject_be.service.impl;

import kr.co.bnk_marketproject_be.dto.OrdersDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseMypageAllOrderDTO;
import kr.co.bnk_marketproject_be.mapper.MypageAllOrderMapper;
import kr.co.bnk_marketproject_be.service.MypageAllOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return orderMapper.findAllOrdersByUserId(userId);
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



}
