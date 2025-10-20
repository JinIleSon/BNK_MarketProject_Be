package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdersMapper {

    // ===== 카트 조회 =====
    List<OrderPageLineRowDTO> selectCartLineRows(@Param("userId") int userId);

    // ===== 주문화면용 쿠폰/포인트/기본배송지 =====
    List<OrderPageCouponViewDTO> selectAvailableCoupons(@Param("userId") int userId);
    Integer selectAvailablePoint(@Param("userId") int userId);
    DeliveriesDTO selectDefaultShipping(@Param("userId") int userId);

    // ===== 체크아웃(주문생성) =====
    void insertOrder(OrdersDTO order);
    void insertOrderItemFromCart(@Param("orderId") int orderId,
                                 @Param("orderItemId") int orderItemId);
    void recalcOrderTotal(@Param("orderId") int orderId);
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

    // ===== 완료 화면 =====
    ProductCompleteDTO selectOrderCompleteHeader(@Param("orderId") int orderId);
    List<OrderPageLineRowDTO> selectOrderLines(@Param("orderId") int orderId);

    // ✅ 사용자의 "결제대기(장바구니)" 주문 id 조회
    Integer selectOpenCartId(@Param("userId") int userId);

    // ✅ 장바구니 아이템 MERGE
    int mergeCartItem(@Param("orderId") int orderId,
                      @Param("productId") int productId,
                      @Param("optionId") Integer optionId,
                      @Param("qty") int qty);

    int deleteCartItems(@Param("orderId") int orderId,
                        @Param("itemIds") List<Integer> itemIds);

    int countItemsInOrder(@Param("orderId") int orderId);

    int deleteOrderIfEmpty(@Param("orderId") int orderId);

    CouponsDTO selectCouponForUser(@Param("userId") int userId,
                                   @Param("couponId") int couponId);

    int finalizeOrder(
            @Param("userId") int userId,
            @Param("orderId") int orderId,
            @Param("totalAmount") int totalAmount
    );

    int markCouponUsed(
            @Param("userId") int userId,
            @Param("couponId") int couponId,
            @Param("orderId") int orderId   // 사용 주문을 기록할 수 있으면 같이 전달
    );
}
