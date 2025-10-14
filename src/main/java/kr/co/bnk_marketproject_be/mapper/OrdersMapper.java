package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdersMapper {
    // 카트(장바구니) 라인들
    List<OrderPageLineRowDTO> selectCartLineRows(int userId);

    // 주문화면용 쿠폰/포인트/기본배송지
    List<OrderPageCouponViewDTO> selectAvailableCoupons(int userId);
    Integer selectAvailablePoint(int userId);
    OrderPageShippingInfoDTO selectDefaultShipping(int userId);

    // 체크아웃(주문생성)
    void insertOrder(OrdersDTO order);

    void insertOrderItemFromCart(@Param("orderId") int orderId,
                                 @Param("orderItemId") int orderItemId);

    void decreaseStock(@Param("productId") int productId,
                       @Param("qty") int qty);

    void insertPayment(PaymentsDTO payment);

    void insertDelivery(DeliveriesDTO delivery);

    void markCouponUsed(@Param("couponId") int couponId,
                        @Param("orderId") int orderId);

    void consumePoint(@Param("userId") int userId,
                      @Param("amount") int amount,
                      @Param("orderId") int orderId);

    void addRewardPoint(@Param("userId") int userId,
                        @Param("amount") int amount,
                        @Param("orderId") int orderId);

    void clearCartByUser(@Param("userId") int userId);

    // ✅ 완료 화면용
    ProductCompleteDTO selectOrderCompleteHeader(@Param("orderId") int orderId);
    List<OrderPageLineRowDTO> selectOrderLines(@Param("orderId") int orderId);


}