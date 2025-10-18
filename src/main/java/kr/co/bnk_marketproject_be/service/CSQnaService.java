package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import kr.co.bnk_marketproject_be.mapper.CSQnaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CSQnaService {

    private final CSQnaMapper qnaMapper;

    // 페이징된 데이터
    public List<CSNoticeDTO> getQnaList(String userid, String boardType2, String boardType3, int offset, int limit) {
        return qnaMapper.selectQnaList(userid, boardType2, boardType3, offset, limit);
    }

    // 전체 게시물 수
    public int getTotalCount(String userid, String boardType2, String boardType3) {
        return qnaMapper.selectTotalCount(userid, boardType2, boardType3);
    }

    public CSNoticeDTO getQnaview(Long id) {
        return qnaMapper.selectQnaview(id);
    }

    public CSNoticeDTO selectCommentView(int bid){
        return qnaMapper.selectCommentView(bid);
    }

    public void insertQna(CSNoticeDTO qna) {
        qnaMapper.insertQna(qna);
    }
}
