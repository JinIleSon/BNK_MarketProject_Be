package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CSQnaMapper {

    // QnA 목록
    List<CSNoticeDTO> selectQnaList(
            @Param("userid") String userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // QnA 상세
    CSNoticeDTO selectQnaview(@Param("id") Long id);

    // QnA 등록
    void insertQna(CSNoticeDTO qna);

}
