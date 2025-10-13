package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.AdminInquiryDTO;
import java.util.List;

public interface AdminInquiryService {
    List<AdminInquiryDTO> latest(int limit);   // 메인에 뿌릴 최근 N건
}
