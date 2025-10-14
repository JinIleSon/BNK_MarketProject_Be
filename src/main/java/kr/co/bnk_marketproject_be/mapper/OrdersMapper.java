package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdersMapper {

    // ===== 카트 조회 =====
    List<OrderPageLineRowDTO> selectCartLineRows(int userId);

    // ===== 장바구니 담기용 =====
    Integer selectDraftCartOrderId(@Param("userId") int userId);
    void insertDraftCart(OrdersDTO draft); // selectKey로 id 채움

    Integer findCartItemIdByKey(@Param("ordersId") int ordersId,
                                @Param("productId") int productId,
                                @Param("optionId") Integer optionId);

    int increaseCartQty(@Param("id") int id, @Param("delta") int delta);

    int insertCartItem(@Param("ordersId") int ordersId,
                       @Param("productId") int productId,
                       @Param("optionId") Integer optionId,
                       @Param("qty") int qty);

    // ===== 주문화면용 쿠폰/포인트/기본배송지 =====
    List<OrderPageCouponViewDTO> selectAvailableCoupons(int userId);
    Integer selectAvailablePoint(int userId);
    OrderPageShippingInfoDTO selectDefaultShipping(int userId);

    // ===== 체크아웃(주문생성) =====
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

    // ===== 완료 화면 =====
    ProductCompleteDTO selectOrderCompleteHeader(@Param("orderId") int orderId);
    List<OrderPageLineRowDTO> selectOrderLines(@Param("orderId") int orderId);

    // ✅ 추가: 사용자의 "결제대기(장바구니)" 주문 id 조회
    Integer selectOpenCartId(@Param("userId") int userId);

    // ✅ 추가: 장바구니 아이템 MERGE (같은 상품/옵션이면 수량 증가, 없으면 새로 삽입)
    int mergeCartItem(@Param("orderId") int orderId,
                      @Param("productId") int productId,
                      @Param("optionId") Integer optionId,
                      @Param("qty") int qty);
}