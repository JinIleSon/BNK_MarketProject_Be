package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import kr.co.bnk_marketproject_be.mapper.CSNoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CSNoticeService {

    private final CSNoticeMapper csNoticeMapper;

    // 공지사항 목록
    public List<CSNoticeDTO> getNoticeList(int page, String boardType) {
        int pageSize = 5;
        int offset = page * pageSize;
        return csNoticeMapper.selectNoticeList(boardType, offset, pageSize);
    }

    // 공지사항 개수
    public int getTotalCount(String boardType) {
        return csNoticeMapper.countNotices(boardType);
    }

    // 공지사항 상세
    public CSNoticeDTO getNoticeDetail(Long id) {
        return csNoticeMapper.selectNoticeDetail(id);
    }
}
