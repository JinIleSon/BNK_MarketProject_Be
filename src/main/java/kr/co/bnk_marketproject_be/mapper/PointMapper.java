package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.MyPagePointLedgerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PointMapper {
    // ✅ 현재 포인트 합계 (balance)
    @Select("""
        SELECT COALESCE(SUM(change_amount), 0)
        FROM point_ledgers
        WHERE users_id = #{userId}
    """)
    long getBalance(String userId);

    // ✅ 포인트 내역 + 누적/전체 포인트
    @Select("""
        SELECT
          id,
          users_id AS userId,
          created_at,
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
    List<MyPagePointLedgerDTO> findLedgers(String userId);
}
