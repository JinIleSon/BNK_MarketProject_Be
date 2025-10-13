package kr.co.bnk_marketproject_be.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.entity.*;
import kr.co.bnk_marketproject_be.repository.custom.AdminOrderRepositoryCustom;
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
public class AdminOrderRepositoryImpl implements AdminOrderRepositoryCustom {

    /**
     * 반드시 파일명을 AdminOrderRepositoryImpl로 해야함.
     * 다른 이름으로 하면 QueryDSL 생성 에러가 발생됨.
     */

    private final JPAQueryFactory jpaQueryFactory;

    private QOrders qOrders = QOrders.orders;
    private QUser qUser = QUser.user;
    private QPayments qPayments = QPayments.payments;


    @Override
    public Page<Tuple> selectAdminOrderAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        // orders_id 별 order_items 개수 서브쿼리
        var itemCountExpr = JPAExpressions
                .select(QOrderItems.orderItems.orders_id.count())
                .from(QOrderItems.orderItems)
                .where(QOrderItems.orderItems.orders_id.eq(qOrders.id));

        List<Tuple> tupleList = jpaQueryFactory.select(qOrders, qUser.userId, qUser.name, qPayments.method, itemCountExpr)
                .from(qOrders)
                .leftJoin(qUser)
                .on(qOrders.users_id.eq(qUser.id))
                .leftJoin(qPayments)
                .on(qPayments.orders_id.eq(qOrders.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qOrders.id.desc())
                .fetch();

        // 전체 게시물 개수
        long total = jpaQueryFactory
                .select(qOrders.count())
                .from(qOrders)
                .fetchOne();

        return new PageImpl<Tuple>(tupleList, pageable, total);
    }

    public Page<Tuple> selectAdminOrderAllForSearch(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        // 검색 타입에 따라 where 조건 표현식 생성(동적 쿼리)
        BooleanExpression expression = null;

        log.info("searchType:{}", searchType);
        log.info("keyword:{}", keyword);
        if (searchType.equals("order_code")) {
            expression = qOrders.order_code.contains(keyword);
        } else if (searchType.equals("userId")) {
            expression = qUser.userId.contains(keyword);
        } else if (searchType.equals("name")) {
            expression = qUser.name.contains(keyword);
            log.info("expression:{}", expression.toString());
        }

        // orders_id 별 order_items 개수 서브쿼리
        var itemCountExpr = JPAExpressions
                .select(QOrderItems.orderItems.orders_id.count())
                .from(QOrderItems.orderItems)
                .where(QOrderItems.orderItems.orders_id.eq(qOrders.id));

        List<Tuple> tupleList = jpaQueryFactory.select(qOrders, qUser.userId, qUser.name, qPayments.method, itemCountExpr)
                .from(qOrders)
                .leftJoin(qUser)
                .on(qOrders.users_id.eq(qUser.id))
                .leftJoin(qPayments)
                .on(qPayments.orders_id.eq(qOrders.id))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qOrders.id.desc())
                .fetch();

        // 전체 게시물 개수
        long total = jpaQueryFactory
                .select(qOrders.count())
                .from(qOrders)
                .leftJoin(qUser)
                .on(qOrders.users_id.eq(qUser.id))
                .leftJoin(qPayments)
                .on(qPayments.orders_id.eq(qOrders.id))
                .where(expression)
                .fetchOne();

        log.info("total:{}", total);
        log.info("tuplelist:{}", tupleList.toString());
        return new PageImpl<Tuple>(tupleList, pageable, total);

    }
}

