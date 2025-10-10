package kr.co.bnk_marketproject_be.service;

import com.querydsl.core.Tuple;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminOrderDTO;
import kr.co.bnk_marketproject_be.dto.OrdersDTO;
import kr.co.bnk_marketproject_be.entity.Orders;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import kr.co.bnk_marketproject_be.repository.AdminOrderRepository;
import kr.co.bnk_marketproject_be.repository.AdminOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminOrderService {

    private final AdminOrderRepository adminOrderRepository;
    private final AdminMapper adminMapper;
    private final ModelMapper modelMapper;

    public PageResponseAdminOrderDTO findAdminOrderAll(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Tuple> pageTuple = null;
        if (pageRequestDTO.getSearchType() != null) {
            // 검색 글 검색
            pageTuple = adminOrderRepository.selectAdminOrderAllForSearch(pageRequestDTO, pageable);
        } else {
            // 일반 글 목록
            pageTuple = adminOrderRepository.selectAdminOrderAllForList(pageRequestDTO, pageable);
        }

        log.info("pageTuple={}", pageTuple);
        List<Tuple> tupleList = pageTuple.getContent();
        int total = (int) pageTuple.getTotalElements();

        List<OrdersDTO> dtoList = tupleList.stream()
                .map(tuple -> {
                    Orders orders = tuple.get(0, Orders.class);
                    String userId = tuple.get(1, String.class);
                    String user_name = tuple.get(2, String.class);
                    String method = tuple.get(3, String.class);
                    Long orders_count_long = tuple.get(4, Long.class);
                    int orders_count = Math.toIntExact(orders_count_long); // int가 꼭 필요하면 변환

                    orders.setUserId(userId);
                    orders.setUser_name(user_name);
                    orders.setMethod(method);
                    orders.setOrders_count(orders_count);

                    log.info("userId={}", userId);
                    log.info("user_name={}", user_name);
                    log.info("method={}", method);
                    log.info("orders_count={}", orders_count);
                    return modelMapper.map(orders, OrdersDTO.class);
                })
                .toList();

        return PageResponseAdminOrderDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public List<OrdersDTO> selectOrders(String order_code){
        return adminMapper.selectOrders(order_code);
    }
}
