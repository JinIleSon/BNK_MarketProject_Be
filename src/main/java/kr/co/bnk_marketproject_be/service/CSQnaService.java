package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import kr.co.bnk_marketproject_be.mapper.CSQnaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CSQnaService {
    //
    private final CSQnaMapper qnaMapper;

    public List<CSNoticeDTO> getQnaList(String userId) {
        return qnaMapper.selectQnaList(userId);
    }

    public CSNoticeDTO getQnaview(Long id) {
        return qnaMapper.selectQnaview(id);
    }

    public void insertQna(CSNoticeDTO qna) {
        qnaMapper.insertQna(qna);
    }

}
