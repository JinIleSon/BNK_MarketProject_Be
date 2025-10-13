package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import kr.co.bnk_marketproject_be.mapper.CSFaqMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CSFaqService {

    private final CSFaqMapper faqMapper;

    public List<CSNoticeDTO> getFaqList() {
        return faqMapper.selectFaqList();
    }

    public CSNoticeDTO getFaqDetail(Long id) {
        return faqMapper.selectFaqDetail(id);
    }

    public void insertFaq(CSNoticeDTO faq) {
        faqMapper.insertFaq(faq);
    }
}


