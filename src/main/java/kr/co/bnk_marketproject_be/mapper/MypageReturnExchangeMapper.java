package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.MypageExchangeRequestDTO;
import kr.co.bnk_marketproject_be.dto.MypageReturnRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
}
