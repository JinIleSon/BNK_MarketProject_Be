package kr.co.bnk_marketproject_be.service;

import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.MyPagePointLedgerDTO;
import kr.co.bnk_marketproject_be.mapper.PointMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {
    private final PointMapper pointMapper;

    public List<MyPagePointLedgerDTO> findLedgers(String userId) {
        return pointMapper.findLedgers(userId);
    }

    // 기존: 숫자 PK로 조회
    public long getBalance(int userId) {
        return pointMapper.findLatestBalanceByNumericId(userId);
    }

    // 추가: 문자열 아이디가 들어와도 안전하게 처리
    public long getBalance(String userId) {
        if (userId == null || userId.isBlank()) return 0L;
        String s = userId.trim();
        if (s.matches("\\d+")) {                    // "123" 처럼 전부 숫자면 PK 취급
            return getBalance(Integer.parseInt(s));
        }
        return pointMapper.findLatestBalanceByUserId(s); // 그 외는 users.user_id 취급
    }
}
