package kr.co.bnk_marketproject_be.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.entity.QDeliveries;
import kr.co.bnk_marketproject_be.entity.QOrderItems;
import kr.co.bnk_marketproject_be.entity.QOrders;
import kr.co.bnk_marketproject_be.entity.QProducts;
import kr.co.bnk_marketproject_be.repository.custom.AdminDeliveryRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminDeliveryRepositoryImpl implements AdminDeliveryRepositoryCustom {

    /**
     * 반드시 파일명을 AdminOrderRepositoryImpl로 해야함.
     * 다른 이름으로 하면 QueryDSL 생성 에러가 발생됨.
     */

    private final JPAQueryFactory jpaQueryFactory;

    private QDeliveries qDeliveries = QDeliveries.deliveries;
    private QOrders qOrders = QOrders.orders;
    private QOrderItems qOrderItems = QOrderItems.orderItems;
    private QProducts qProducts = QProducts.products;

    @Override
    public Page<Tuple> selectAdminDeliveryAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        var itemCountWithDeliveryJoin = JPAExpressions.select(QOrderItems.orderItems.orders_id.count())
                .from(QOrderItems.orderItems)
                .leftJoin(QDeliveries.deliveries).on(QDeliveries.deliveries.orders_id.eq(QOrderItems.orderItems.orders_id))
                .where(QOrderItems.orderItems.orders_id.eq(QOrders.orders.id));


        // 최소 order_item id (주문별)
        var minOiIdSub = JPAExpressions
                .select(qOrderItems.id.min())
                .from(qOrderItems)
                .where(qOrderItems.orders_id.eq(qOrders.id));

        // 최소 id의 상품명 1개
        var firstProductNameSub = JPAExpressions
                .select(qProducts.product_name)
                .from(qOrderItems)
                .join(qProducts).on(qOrderItems.products_id.eq(qProducts.id))
                .where(qOrderItems.id.eq(minOiIdSub));

        List<Tuple> tupleList = jpaQueryFactory.select(qDeliveries,
                        qOrders.order_code, firstProductNameSub, qOrders.total_amount, itemCountWithDeliveryJoin)
                .from(qDeliveries)
                .leftJoin(qOrders)
                .on(qDeliveries.orders_id.eq(qOrders.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qDeliveries.id.desc())
                .fetch();

        // 전체 게시물 개수
        long total = jpaQueryFactory
                .select(qDeliveries.count())
                .from(qDeliveries)
                .fetchOne();

        return new PageImpl<Tuple>(tupleList, pageable, total);
    }

    public Page<Tuple> selectAdminDeliveryAllForSearch(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        // 검색 타입에 따라 where 조건 표현식 생성(동적 쿼리)
        BooleanExpression expression = null;

        log.info("searchType:{}", searchType);
        log.info("keyword:{}", keyword);
        if (searchType.equals("invoice")) {
            expression = qDeliveries.invoice.contains(keyword);
        } else if (searchType.equals("order_code")) {
            expression = qOrders.order_code.contains(keyword);
        } else if (searchType.equals("recipient")) {
            expression = qDeliveries.recipient.contains(keyword);
            log.info("expression:{}", expression.toString());
        }

        // 최소 order_item id (주문별)
        var minOiIdSub = JPAExpressions
                .select(qOrderItems.id.min())
                .from(qOrderItems)
                .where(qOrderItems.orders_id.eq(qOrders.id));

        // 최소 id의 상품명 1개
        var firstProductNameSub = JPAExpressions
                .select(qProducts.product_name)
                .from(qOrderItems)
                .join(qProducts).on(qOrderItems.products_id.eq(qProducts.id))
                .where(qOrderItems.id.eq(minOiIdSub));

        var itemCountWithDeliveryJoin = JPAExpressions.select(QOrderItems.orderItems.orders_id.count())
                .from(QOrderItems.orderItems)
                .leftJoin(QDeliveries.deliveries).on(QDeliveries.deliveries.orders_id.eq(QOrderItems.orderItems.orders_id))
                .where(QOrderItems.orderItems.orders_id.eq(QOrders.orders.id));


        List<Tuple> tupleList = jpaQueryFactory.select(qDeliveries,
                        qOrders.order_code, firstProductNameSub, qOrders.total_amount, itemCountWithDeliveryJoin)
                .from(qDeliveries)
                .leftJoin(qOrders)
                .on(qDeliveries.orders_id.eq(qOrders.id))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qDeliveries.id.desc())
                .fetch();

        // 전체 게시물 개수
        long total = jpaQueryFactory
                .select(qDeliveries.id.countDistinct())
                .from(qDeliveries)
                .leftJoin(qOrders).on(qDeliveries.orders_id.eq(qOrders.id))
                .where(expression) // 검색 쿼리에서만
                .fetchOne();

        log.info("total:{}", total);
        log.info("tuplelist:{}", tupleList.toString());
        return new PageImpl<Tuple>(tupleList, pageable, total);

    }
}