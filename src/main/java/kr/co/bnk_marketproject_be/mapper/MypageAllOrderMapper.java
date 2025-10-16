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

    // 사용자명으로 user_id 조회
    @Select("SELECT id FROM USERS WHERE USER_ID = #{username}")
    int findUserIdByUsername(String username);

    // 전체 주문 목록 조회
    List<OrdersDTO> findAllOrdersByUserId(@Param("userId") String userId);

    // 단일 주문 상세
    OrdersDTO findOrderDetailById(@Param("orderId") int orderId);

    // 페이징 주문 목록
    List<OrdersDTO> findPagedOrders(@Param("userId") String userId,
                                    @Param("pageRequest") PageRequestDTO pageRequest);

    int countOrdersByUserId(@Param("userId") String userId);

    // 주문별 상품 목록 조회
    List<OrderItemsDTO> findOrderItemsByOrderId(@Param("orderId") int orderId);
    // 상품평 등록
    int insertProductBoard(ProductBoardsDTO dto);
}
