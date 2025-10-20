package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.MyPagePointLedgerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Mapper
public interface PointMapper {
    // ✅ 현재 포인트 합계 (balance)
    // users.id(숫자 PK)로 조회
    @Select("""
        SELECT NVL(balance, 0)
          FROM ( SELECT balance
                   FROM point_ledgers
                  WHERE users_id = (SELECT u.user_id FROM users u WHERE u.id = #{userId})
                  ORDER BY created_at DESC, id DESC )
         WHERE ROWNUM = 1
    """)
    Long findLatestBalanceByNumericId(@Param("userId") long userId);

    // users.user_id(문자 아이디)로 조회
    @Select("""
        SELECT NVL(balance, 0)
          FROM ( SELECT balance
                   FROM point_ledgers
                  WHERE users_id = #{userId}
                  ORDER BY created_at DESC, id DESC )
         WHERE ROWNUM = 1
    """)
    Long findLatestBalanceByUserId(@Param("userId") String userId);

    @Select("""
        SELECT
          id, users_id AS userId, created_at,
          SUM(change_amount) OVER (
            PARTITION BY users_id
            ORDER BY created_at, id
            ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
          ) AS sumPoint,
          SUM(change_amount) OVER (PARTITION BY users_id) AS totalPoint,
          change_amount AS changeAmount,
          description
        FROM point_ledgers
        WHERE users_id = #{userId}
        ORDER BY created_at DESC, id DESC
    """)
    List<MyPagePointLedgerDTO> findLedgers(@Param("userId") String userId);

}
