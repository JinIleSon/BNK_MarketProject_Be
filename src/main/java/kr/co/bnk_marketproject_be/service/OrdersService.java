package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.mapper.OrdersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersMapper ordersMapper;

    private String generateOrderCode() {
        return "O" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 장바구니
     */
    @Transactional(readOnly = true)
    public ProductCartDTO getCart(int userId) {
        var rows = ordersMapper.selectCartLineRows(userId);

        int subtotal = 0, discount = 0, delicharTotal = 0;   int itemCount = 0; // ✅ 수량 합계

        for (var r : rows) {
            int unit = r.getUnitPrice();          // 정가
            int rate = r.getDiscountRate();       // 0~100 가정(쿼리에서 보장)

            // 안전 가드
            if (rate < 0) rate = 0;
            if (rate > 100) rate = 100;

            // 1개당 할인액, 판매가
            int unitDiscount = (int) Math.round(unit * (rate / 100.0));
            int sale = unit - unitDiscount;

            // 라인 합계(판매가 * 수량)
            int line = sale * r.getQuantity();
            r.setLineTotal(line);

            // 배송비/무료배송
            Integer deli = r.getDelichar();
            int deliVal = (deli == null ? 0 : deli);
            boolean free = (deliVal == 0);
            r.setFreeShipping(free);

            // 합계 집계
            subtotal += unit * r.getQuantity();          // 정가 합
            discount += unitDiscount * r.getQuantity();  // 총 할인액
            if (!free) delicharTotal += deliVal;
            itemCount += r.getQuantity();
        }

        var summary = OrderPageSummaryDTO.builder()
                .itemCount(itemCount)
                .subtotalAmount(subtotal)
                .discountAmount(discount)                // 우측 하단 “할인금액”
                .delichar(delicharTotal)
                .couponAmount(0)
                .pointUse(0)
                .totalPayable(subtotal - discount + delicharTotal)
                .rewardPoint((int) Math.floor((subtotal - discount) * 0.01)) // 1% 적립 예시
                .build();

        return ProductCartDTO.builder()
                .items(rows)
                .summary(summary)
                .selectable(true)
                .build();
    }

    /**
     * 주문 화면
     */
    @Transactional(readOnly = true)
    public ProductOrderDTO getOrderPage(int userId) {
        var cart = getCart(userId);
        var coupons = ordersMapper.selectAvailableCoupons(userId);
        var point = ordersMapper.selectAvailablePoint(userId);
        var ship = ordersMapper.selectDefaultShipping(userId);

        return ProductOrderDTO.builder()
                .items(cart.getItems())
                .summary(cart.getSummary())
                .availableCoupons(coupons)
                .availablePoint(point == null ? 0 : point)
                .paymentMethods(List.of("신용카드", "계좌이체", "휴대폰결제", "카카오페이"))
                .defaultShipping(ship)
                .build();
    }

    /**
     * 체크아웃
     */
    @Transactional
    public int checkout(int userId, OrderPageSubmitDTO submit) {   // ✅ String → int
        var cart = getCart(userId);

        int couponAmt = 0;
        if (submit.getCoupon_id() != 0) { /* 쿠폰 검증/계산 */ }
        int pointUse = Math.min(submit.getUsePoint(), cart.getSummary().getTotalPayable());
        int finalPay = Math.max(0, cart.getSummary().getTotalPayable() - couponAmt - pointUse);

        var order = OrdersDTO.builder()
                .users_id(userId)
                .status("결제완료")
                .total_amount(finalPay)
                .order_code(generateOrderCode())
                .build();
        ordersMapper.insertOrder(order);        // ✅ order.id 세팅

        int orderId = order.getId();            // ✅ 반환할 PK

        // 아이템/재고/결제/배송/쿠폰/포인트 처리 (기존 로직 유지)
        for (var r : cart.getItems()) {
            ordersMapper.insertOrderItemFromCart(orderId, r.getOrder_item_id());
            ordersMapper.decreaseStock(r.getId(), r.getQuantity());
        }

        // ✅ 새로 생성한 주문(orderId)에 대해 총액 재계산
        ordersMapper.recalcOrderTotal(orderId);

        ordersMapper.insertPayment(PaymentsDTO.builder()
                .orders_id(orderId)
                .amount(finalPay)
                .method(submit.getPayment_method())
                .status("성공").build());

        var ship = submit.getShipping();
        ordersMapper.insertDelivery(DeliveriesDTO.builder()
                .orders_id(orderId)
                .recipient(ship.getRecipient())
                .delicom(ship.getDelicom())
                .zipcode(ship.getZipcode())
                .address(ship.getAddress())
                .address2(ship.getAddress2())
                .delichar(cart.getSummary().getDelichar())
                .status("배송준비")
                .build());

        if (submit.getCoupon_id() != 0) ordersMapper.markCouponUsed(submit.getCoupon_id(), orderId);
        if (pointUse > 0) ordersMapper.consumePoint(userId, pointUse, orderId);
        ordersMapper.addRewardPoint(userId, cart.getSummary().getRewardPoint(), orderId);
        ordersMapper.clearCartByUser(userId);

        return orderId;                        // ✅ 이제 PK 반환
    }


    /**
     * 주문완료 화면
     */
    @Transactional(readOnly = true)
    public ProductCompleteDTO getComplete(int orderId) {           // ✅ String → int
        var header = ordersMapper.selectOrderCompleteHeader(orderId); // ✅ orderId로 조회
        var lines = ordersMapper.selectOrderLines(orderId);
        header.setItems(lines);

        int subtotal = 0, discount = 0, delichar = 0;
        for (var r : lines) {
            int sale = r.getUnitPrice() - (r.getUnitPrice() * r.getDiscountRate() / 100);
            subtotal += r.getUnitPrice() * r.getQuantity();
            discount += (r.getUnitPrice() - sale) * r.getQuantity();
            if (r.getDelichar() != null && r.getDelichar() > 0) delichar += r.getDelichar();
        }
        header.setSummary(OrderPageSummaryDTO.builder()
                .itemCount(lines.size())
                .subtotalAmount(subtotal)
                .discountAmount(discount)
                .delichar(delichar)
                .totalPayable(subtotal - discount + delichar)
                .build());
        return header;
    }

    /**
     * 장바구니 담기: 결제대기(장바구니) 주문을 보장하고, 같은 상품(+옵션)은 수량만 증가
     * @return 영향 행 수(성공 시 1 이상)
     */
    @Transactional
    public int addToCart(int uid, int productId, Integer optionId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");

        // 1) 사용자의 "결제대기(장바구니)" 주문 id 조회
        Integer cartOrderId = ordersMapper.selectOpenCartId(uid);

        // 2) 없으면 생성
        if (cartOrderId == null) {
            OrdersDTO order = OrdersDTO.builder()
                    .users_id(uid)
                    .status("결제대기")
                    .total_amount(0)
                    .order_code(generateOrderCode())
                    .build();
            ordersMapper.insertOrder(order); // keyProperty="id"로 PK 채워짐
            cartOrderId = order.getId();
        }

        // 3) 아이템 MERGE(같은 상품/옵션이면 qty += ?, 없으면 INSERT)
        int affected = ordersMapper.mergeCartItem(cartOrderId, productId, optionId, qty);

// ✅ 장바구니 담기 직후 orders.total_amount 재계산
        ordersMapper.recalcOrderTotal(cartOrderId);

        return affected;
    }

    @Transactional
    public int removeFromCart(int userId, List<Integer> itemIds) {
        Integer cartOrderId = ordersMapper.selectOpenCartId(userId);
        if (cartOrderId == null) return 0;

        int removed = ordersMapper.deleteCartItems(cartOrderId, itemIds);

        // 합계 재계산
        ordersMapper.recalcOrderTotal(cartOrderId);

        // (선택) 아이템이 0개면 빈 주문 레코드 정리
        if (ordersMapper.countItemsInOrder(cartOrderId) == 0) {
            ordersMapper.deleteOrderIfEmpty(cartOrderId);
        }
        return removed;
    }
}