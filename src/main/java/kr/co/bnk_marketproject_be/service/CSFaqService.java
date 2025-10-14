package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import kr.co.bnk_marketproject_be.mapper.CSFaqMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CSFaqService {

    private final CSFaqMapper faqMapper;

    // Faq List
    public List<CSNoticeDTO> getFaqList(int offset, int limit) {
        Map<String,Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        return faqMapper.selectFaqList(params);
    }

    public List<CSNoticeDTO> getFaqListByType(String boardType, int offset, int limit) {
        Map<String,Object> params = new HashMap<>();
        params.put("boardType", boardType);
        params.put("offset", offset);
        params.put("limit", limit);
        return faqMapper.selectFaqListByType(params);
    }

    // Faq view
    public CSNoticeDTO getFaqView(Long id) {
        return faqMapper.selectFaqView(id);
    }

}


