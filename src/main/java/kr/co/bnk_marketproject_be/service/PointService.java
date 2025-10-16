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

    // 전체 포인트 내역 조회
    public List<MyPagePointLedgerDTO> findLedgers(String userId) {
        return pointMapper.findLedgers(userId);
    }

    // 현재 포인트 합계 (balance)
    public long getBalance(String userId) {
        return pointMapper.getBalance(userId);
    }
}
