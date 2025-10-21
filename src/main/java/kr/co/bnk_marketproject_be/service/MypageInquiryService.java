package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.MypageInquiryDTO;
import kr.co.bnk_marketproject_be.mapper.MypageInquiryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageInquiryService {

    private final MypageInquiryMapper inquiryMapper;

    public void createInquiry(MypageInquiryDTO dto) {
        inquiryMapper.insertInquiry(dto);
    }
}
