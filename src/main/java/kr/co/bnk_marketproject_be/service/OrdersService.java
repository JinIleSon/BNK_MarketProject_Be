package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.mapper.OrdersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersMapper ordersMapper;

    private String generateOrderCode() {
        return "O" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /** 장바구니 */
    @Transactional(readOnly = true)
    public ProductCartDTO getCart(int userId) {
        var rows = ordersMapper.selectCartLineRows(userId);

        int subtotal = 0, discount = 0, delicharTotal = 0, itemCount = 0;

        for (var r : rows) {
            int unit = r.getUnitPrice();
            int rate = Math.max(0, Math.min(100, r.getDiscountRate()));

            int unitDiscount = (int) Math.round(unit * (rate / 100.0));
            int sale = unit - unitDiscount;

            int line = sale * r.getQuantity();
            r.setLineTotal(line);

            Integer deli = r.getDelichar();
            int deliVal = (deli == null ? 0 : deli);
            boolean free = (deliVal == 0);
            r.setFreeShipping(free);

            subtotal  += unit * r.getQuantity();
            discount  += unitDiscount * r.getQuantity();

            // 라인당 1회만 합산(수량과 무관)
            if (!free) delicharTotal += deliVal;

            itemCount += r.getQuantity();
        }

        var summary = OrderPageSummaryDTO.builder()
                .itemCount(itemCount)
                .subtotalAmount(subtotal)
                .discountAmount(discount)
                .delichar(delicharTotal)
                .couponAmount(0)
                .pointUse(0)
                .totalPayable(subtotal - discount + delicharTotal)
                .rewardPoint((int) Math.floor((subtotal - discount) * 0.01))
                .build();

        return ProductCartDTO.builder()
                .items(rows)
                .summary(summary)
                .selectable(true)
                .build();
    }

    /** 주문 화면 */
    @Transactional(readOnly = true)
    public ProductOrderDTO getOrderPage(int userId) {
        var cart    = getCart(userId);
        var coupons = ordersMapper.selectAvailableCoupons(userId);
        var point   = ordersMapper.selectAvailablePoint(userId);     // 최근 balance

        // 반환 타입 DeliveriesDTO
        DeliveriesDTO ship = ordersMapper.selectDefaultShipping(userId);

        return ProductOrderDTO.builder()
                .items(cart.getItems())
                .summary(cart.getSummary())
                .availableCoupons(coupons)
                .availablePoint(point == null ? 0 : point)
                .paymentMethods(List.of("신용카드", "계좌이체", "휴대폰결제", "카카오페이"))
                .defaultShipping(ship)
                .build();
    }

    /** 체크아웃 */
    @Transactional
    public int checkout(int userId, OrderPageSubmitDTO submit) {
        var cart = getCart(userId);

        // 장바구니 기준 값
        int subtotal     = cart.getSummary().getSubtotalAmount();
        int prodDiscount = cart.getSummary().getDiscountAmount();
        int shipping     = cart.getSummary().getDelichar();

        // 1) 쿠폰 검증/할인액
        int couponAmt = 0;
        int couponId  = submit.getCoupon_id();
        if (couponId != 0) {
            CouponsDTO c = ordersMapper.selectCouponForUser(userId, couponId);
            if (c == null) throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");

            int base = Math.max(0, subtotal - prodDiscount);
            if ("RATE".equalsIgnoreCase(c.getDiscount_type())) {
                couponAmt = (int) Math.floor(base * (c.getDiscount_value() / 100.0));
            } else {
                couponAmt = Math.min(c.getDiscount_value(), base);
            }
        }

        // 2) 포인트 사용
        int payableBeforePoint = Math.max(0, subtotal - prodDiscount - couponAmt + shipping);
        Integer latestBalance = ordersMapper.selectAvailablePoint(userId);
        int availablePoint    = Math.max(0, latestBalance == null ? 0 : latestBalance);
        int requestedUsePoint = Math.max(0, submit.getUsePoint());
        int pointUse = Math.min(requestedUsePoint, Math.min(availablePoint, payableBeforePoint));

        // 3) 최종 결제 금액
        int finalPay = Math.max(0, payableBeforePoint - pointUse);

        // 4) 기존 장바구니 주문 id
        Integer orderIdObj = ordersMapper.selectOpenCartId(userId);
        if (orderIdObj == null) throw new IllegalStateException("결제대기 중인 주문이 없습니다.");
        int orderId = orderIdObj;

        // 5) 헤더를 '결제완료'로 승격 (새 INSERT 금지)
        int affected = ordersMapper.finalizeOrder(userId, orderId, finalPay);
        if (affected == 0) throw new IllegalStateException("주문 상태 변경 실패(동시성 또는 상태 불일치).");

        // 6) 재고 차감
        for (var r : cart.getItems()) {
            ordersMapper.decreaseStock(r.getId(), r.getQuantity()); // r.getId() = products.id
        }

        // 7) 결제 정보
        String payMethod = submit.getPayment_method();
        if (payMethod == null || payMethod.isBlank()) {
            try {
                var m = submit.getClass().getMethod("getPay");
                Object v = m.invoke(submit);
                if (v != null) payMethod = String.valueOf(v);
            } catch (Exception ignore) {}
        }
        if (payMethod == null || payMethod.isBlank()) payMethod = "신용카드";

        ordersMapper.insertPayment(PaymentsDTO.builder()
                .orders_id(orderId)
                .amount(finalPay)
                .method(payMethod)
                .status("결제완료")
                .build());

        // 8) 배송 정보 스냅샷(장바구니에서 계산한 배송비 저장)
        var ship = submit.getShipping();
        ordersMapper.insertDelivery(DeliveriesDTO.builder()
                .orders_id(orderId)
                .recipient(ship.getRecipient())
                .delicom(ship.getDelicom())
                .zipcode(ship.getZipcode())
                .address(ship.getAddress())
                .address2(ship.getAddress2())
                .delichar(shipping)   // ← 라인별 합산된 배송비 스냅샷
                .status("배송준비")
                .note(ship.getMemo())
                .build());

        // 9) 쿠폰/포인트 반영
        if (couponId != 0) ordersMapper.markCouponUsed(userId, couponId, orderId);
        if (pointUse > 0)  ordersMapper.consumePoint(userId, pointUse, orderId);

        // 10) 적립 포인트
        int reward = (int) Math.floor(finalPay * 0.01);
        if (reward > 0) ordersMapper.addRewardPoint(userId, reward, orderId);

        return orderId;
    }

    /** 주문완료 화면 */
    @Transactional(readOnly = true)
    public ProductCompleteDTO getComplete(int orderId) {
        var header = ordersMapper.selectOrderCompleteHeader(orderId);
        if (header == null) throw new IllegalStateException("주문을 찾을 수 없습니다: " + orderId);

        var lines = ordersMapper.selectOrderLines(orderId);

        int subtotal = 0;
        int itemsDiscount = 0;
        int totalQty = 0;

        // ✅ 배송비: "각 라인에 1회"씩 합산(수량과 무관)
        int delichar = 0;

        for (var r : lines) {
            int unit = r.getUnitPrice();

            int rate = Math.max(0, Math.min(100, r.getDiscountRate()));
            int unitDiscount = (r.getUnitDiscount() != null)
                    ? r.getUnitDiscount()
                    : (int) Math.round(unit * (rate / 100.0));

            int unitSale = (r.getUnitSalePrice() != null)
                    ? r.getUnitSalePrice()
                    : unit - unitDiscount;

            // lineTotal은 primitive int 이므로 null 비교 금지 → 항상 재계산
            int lineTotal = unitSale * r.getQuantity();

            r.setUnitDiscount(unitDiscount);
            r.setUnitSalePrice(unitSale);
            r.setLineTotal(lineTotal);

            subtotal      += unit * r.getQuantity();
            itemsDiscount += unitDiscount * r.getQuantity();
            totalQty      += r.getQuantity();

            Integer lineDeli = r.getDelichar();
            if (lineDeli != null && lineDeli > 0) {
                delichar += lineDeli; // 라인당 1회만 합산
            }
        }
        header.setItems(lines);

        // ✅ 확정된 사용 포인트/쿠폰 금액 조회 (없으면 0)
        Integer usedPoint  = ordersMapper.selectUsedPoint(orderId);
        Integer usedCoupon = ordersMapper.selectUsedCouponAmount(orderId);
        int pointUse  = usedPoint  == null ? 0 : usedPoint;
        int couponAmt = usedCoupon == null ? 0 : usedCoupon;

        // ✅ 합계(확정 기준)
        int totalPayable = Math.max(0, subtotal - itemsDiscount - couponAmt - pointUse + delichar);

        var sum = OrderPageSummaryDTO.builder()
                .itemCount(lines.size())
                .totalQuantity(totalQty)
                .subtotalAmount(subtotal)
                .discountAmount(itemsDiscount) // 상품 자체 할인 합
                .couponAmount(couponAmt)
                .pointUse(pointUse)
                .delichar(delichar)            // ← 라인별 배송비 합산
                .totalPayable(totalPayable)
                .rewardPoint((int) Math.floor(totalPayable * 0.01))
                .itemsDiscountAmount(itemsDiscount)
                .build();

        header.setSummary(sum);
        header.setOrderId(orderId);
        return header;
    }

    /** 장바구니 담기 */
    @Transactional
    public int addToCart(int uid, int productId, Integer optionId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");

        Integer cartOrderId = ordersMapper.selectOpenCartId(uid);

        if (cartOrderId == null) {
            OrdersDTO order = OrdersDTO.builder()
                    .users_id(uid)
                    .status("결제대기")
                    .total_amount(0)
                    .order_code(generateOrderCode())
                    .build();
            ordersMapper.insertOrder(order);
            cartOrderId = order.getId();
        }

        int affected = ordersMapper.mergeCartItem(cartOrderId, productId, optionId, qty);
        ordersMapper.recalcOrderTotal(cartOrderId);
        return affected;
    }

    @Transactional
    public int removeFromCart(int userId, List<Integer> itemIds) {
        Integer cartOrderId = ordersMapper.selectOpenCartId(userId);
        if (cartOrderId == null) return 0;

        int removed = ordersMapper.deleteCartItems(cartOrderId, itemIds);
        ordersMapper.recalcOrderTotal(cartOrderId);

        if (ordersMapper.countItemsInOrder(cartOrderId) == 0) {
            ordersMapper.deleteOrderIfEmpty(cartOrderId);
        }
        return removed;
    }
}
