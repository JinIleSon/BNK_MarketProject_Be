package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.MypageExchangeRequestDTO;
import kr.co.bnk_marketproject_be.dto.MypageReturnRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MypageReturnExchangeMapper {

    // ✅ 반품신청 등록
    void insertReturnRequest(MypageReturnRequestDTO dto);

    // ✅ 교환신청 등록
    int insertExchangeRequest(MypageExchangeRequestDTO dto);

    // ✅ 반품신청 목록 조회
    List<MypageReturnRequestDTO> findReturnList(Long userId);

    // ✅ 교환신청 목록 조회
    List<MypageExchangeRequestDTO> findExchangeList(Long userId);

    // ✅ (신규 추가) 교환신청 모달용 상품 상세 조회
    Map<String, Object> findOrderItemDetail(Long orderItemId);
}
