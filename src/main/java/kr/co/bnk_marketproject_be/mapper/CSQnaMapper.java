package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CSQnaMapper {

    // QnA 목록
    List<CSNoticeDTO> selectQnaList(@Param("userId") String userId);

    // QnA 상세
    CSNoticeDTO selectQnaDetail(@Param("id") Long id);

    // QnA 등록
    void insertQna(CSNoticeDTO qna);

    // 관리자 답변 등록/수정
    void updateAnswer(CSNoticeDTO qna);
}
