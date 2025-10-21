package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.*;

import java.util.List;
import java.util.Map;

public interface MypageReturnExchangeService {
    void insertReturnRequest(MypageReturnRequestDTO dto);
    void insertExchangeRequest(MypageExchangeRequestDTO dto);
    List<MypageReturnRequestDTO> findReturnList(Long userId);
    List<MypageExchangeRequestDTO> findExchangeList(Long userId);
    Map<String, Object> findOrderItemDetail(Long orderItemId);
}
