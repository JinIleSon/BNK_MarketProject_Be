package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.MypageInquiryDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MypageInquiryMapper {
    int insertInquiry(MypageInquiryDTO dto);
}
