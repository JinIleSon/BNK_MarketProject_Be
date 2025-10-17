package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.AdminStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminStatsRepository extends JpaRepository<AdminStats, Long> {

    // =========================
    // 1) 카테고리별 매출 집계 (파이 차트)
    // =========================
    // ORDERS(결제완료) + ORDER_ITEMS + PRODUCTS + CATEGORIES
    @Query(value = """
    SELECT c.NAME AS category,
           NVL(SUM(NVL(oi.QUANTITY, 1) * NVL(oi.PRICE, p.PRICE)), 0) AS amount
      FROM NICHIYA.ORDERS       o
      JOIN NICHIYA.ORDER_ITEMS  oi ON oi.ORDERS_ID   = o.ID
      JOIN NICHIYA.PRODUCTS     p  ON p.ID           = oi.PRODUCTS_ID
      JOIN NICHIYA.CATEGORIES   c  ON c.ID           = p.CATEGORIES_ID
     WHERE o.STATUS = '결제완료'
       AND o.CREATED_AT BETWEEN :startAt AND :endAt
     GROUP BY c.NAME
     ORDER BY amount DESC
""", nativeQuery = true)
    List<Object[]> aggregateCategorySales(@Param("startAt") LocalDateTime startAt,
                                          @Param("endAt")   LocalDateTime endAt);


    // =========================
    // 2) 일자별 주문/결제/취소 집계 (막대 차트)
    // =========================
    // 결과: [ymd, order_cnt, pay_cnt, cancel_cnt]
    //  - ymd: 'YYYY-MM-DD' 문자열
    //  - order_cnt: 해당일 총 주문수
    //  - pay_cnt: 해당일 '결제완료' 건수
    //  - cancel_cnt: 해당일 '취소' 계열 건수 (현장 규칙에 맞게 상태 값 조정)
    @Query(value = """
        SELECT TO_CHAR(TRUNC(o.CREATED_AT), 'YYYY-MM-DD') AS ymd,
               COUNT(*) AS order_cnt,
               SUM(CASE WHEN o.STATUS = '결제완료' THEN 1 ELSE 0 END) AS pay_cnt,
               SUM(CASE WHEN o.STATUS IN ('취소','취소완료') THEN 1 ELSE 0 END) AS cancel_cnt
          FROM ORDERS o
         WHERE o.CREATED_AT BETWEEN :startAt AND :endAt
         GROUP BY TRUNC(o.CREATED_AT)
         ORDER BY TRUNC(o.CREATED_AT)
        """, nativeQuery = true)
    List<Object[]> aggregateDailyOps(@Param("startAt") LocalDateTime startAt,
                                     @Param("endAt")   LocalDateTime endAt);

    // =========================
    // 3) 운영 지표 숫자 카드
    // =========================
    @Query(value = """
        SELECT COUNT(*) FROM ORDERS 
         WHERE STATUS = '입금대기' 
           AND CREATED_AT BETWEEN :startAt AND :endAt
        """, nativeQuery = true)
    long countPendingPayment(@Param("startAt") LocalDateTime startAt,
                             @Param("endAt")   LocalDateTime endAt);

    @Query(value = """
        SELECT COUNT(*) FROM ORDERS 
         WHERE STATUS = '배송준비' 
           AND CREATED_AT BETWEEN :startAt AND :endAt
        """, nativeQuery = true)
    long countShippingReady(@Param("startAt") LocalDateTime startAt,
                            @Param("endAt")   LocalDateTime endAt);

    @Query(value = """
        SELECT COUNT(*) FROM ORDERS 
         WHERE STATUS = '취소요청' 
           AND CREATED_AT BETWEEN :startAt AND :endAt
        """, nativeQuery = true)
    long countCancelRequest(@Param("startAt") LocalDateTime startAt,
                            @Param("endAt")   LocalDateTime endAt);

    @Query(value = """
        SELECT COUNT(*) FROM ORDERS 
         WHERE STATUS = '교환요청' 
           AND CREATED_AT BETWEEN :startAt AND :endAt
        """, nativeQuery = true)
    long countExchangeRequest(@Param("startAt") LocalDateTime startAt,
                              @Param("endAt")   LocalDateTime endAt);

    @Query(value = """
        SELECT COUNT(*) FROM ORDERS 
         WHERE STATUS = '반품요청' 
           AND CREATED_AT BETWEEN :startAt AND :endAt
        """, nativeQuery = true)
    long countReturnRequest(@Param("startAt") LocalDateTime startAt,
                            @Param("endAt")   LocalDateTime endAt);

    @Query(value = """
        SELECT COUNT(*) FROM ORDERS 
         WHERE CREATED_AT BETWEEN :startAt AND :endAt
        """, nativeQuery = true)
    long countOrders(@Param("startAt") LocalDateTime startAt,
                     @Param("endAt")   LocalDateTime endAt);

    @Query(value = """
        SELECT NVL(SUM(TOTAL_AMOUNT), 0) FROM ORDERS 
         WHERE CREATED_AT BETWEEN :startAt AND :endAt
        """, nativeQuery = true)
    long sumOrderAmount(@Param("startAt") LocalDateTime startAt,
                        @Param("endAt")   LocalDateTime endAt);

    // 회원 가입 수 (USERS)
    @Query(value = """
        SELECT COUNT(*) FROM USERS 
         WHERE CREATED_AT BETWEEN :startAt AND :endAt
        """, nativeQuery = true)
    long countUserSignups(@Param("startAt") LocalDateTime startAt,
                          @Param("endAt")   LocalDateTime endAt);

    // 문의 게시물 수 (BOARD)
    @Query(value = """
        SELECT COUNT(*) FROM BOARD 
         WHERE UPPER(BOARD_TYPE) = 'INQUIRY'
           AND CREATED_AT BETWEEN :startAt AND :endAt
        """, nativeQuery = true)
    long countQna(@Param("startAt") LocalDateTime startAt,
                  @Param("endAt")   LocalDateTime endAt);
}
