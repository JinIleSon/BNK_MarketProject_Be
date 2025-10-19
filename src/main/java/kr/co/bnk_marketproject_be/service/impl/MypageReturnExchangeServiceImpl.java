package kr.co.bnk_marketproject_be.service.impl;

import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.mapper.MypageReturnExchangeMapper;
import kr.co.bnk_marketproject_be.service.MypageReturnExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MypageReturnExchangeServiceImpl implements MypageReturnExchangeService {

    private final MypageReturnExchangeMapper mapper;

    @Override
    public void insertReturnRequest(MypageReturnRequestDTO dto) {
        log.info("📦 [Service] 반품신청 저장: {}", dto);
        mapper.insertReturnRequest(dto);
    }

    @Override
    @Transactional
    public void insertExchangeRequest(MypageExchangeRequestDTO dto) {
        log.info("🔁 [Service] 교환신청 저장: {}", dto);
        int result = mapper.insertExchangeRequest(dto);
        log.info("✅ [MyBatis Insert 결과] result={}", result);
    }



    @Override
    public List<MypageReturnRequestDTO> findReturnList(Long userId) {
        log.info("📋 [Service] 반품신청 목록 조회 userId={}", userId);
        return mapper.findReturnList(userId);
    }

    @Override
    public List<MypageExchangeRequestDTO> findExchangeList(Long userId) {
        log.info("📋 [Service] 교환신청 목록 조회 userId={}", userId);
        return mapper.findExchangeList(userId);
    }
}
