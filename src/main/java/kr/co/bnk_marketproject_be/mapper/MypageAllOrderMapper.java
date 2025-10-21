package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.OrderItemsDTO;
import kr.co.bnk_marketproject_be.dto.OrdersDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.ProductBoardsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MypageAllOrderMapper {

    @Select("SELECT id FROM USERS WHERE USER_ID = #{username}")
    int findUserIdByUsername(String username);

    List<OrdersDTO> findAllOrdersByUserId(@Param("userId") String userId);

    List<OrdersDTO> findOrderDetailByCode(@Param("orderCode") String orderCode);

    List<OrdersDTO> findPagedOrders(@Param("userId") String userId,
                                    @Param("pageRequest") PageRequestDTO pageRequest);

    int countOrdersByUserId(@Param("userId") String userId);

    int insertProductBoard(ProductBoardsDTO dto);

    List<OrdersDTO> findRecentOrdersByUserId(@Param("userId") String userId);
}
