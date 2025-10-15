package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.OrdersDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MypageAllOrderMapper {

    //전체 주문 목록 조화
    List<OrdersDTO> findAllOrdersByUserId(String userId);

    //특정 주문 상세 조회
    OrdersDTO findOrderDetailById(int orderId);

    @Select("SELECT id FROM USERS WHERE USER_ID = #{username}")
    int findUserIdByUsername(String username);

    List<OrdersDTO> findPagedOrders(@Param("userId") String userId,
                                    @Param("pageRequest") PageRequestDTO pageRequest);

    int countOrdersByUserId(@Param("userId") String userId);

}
