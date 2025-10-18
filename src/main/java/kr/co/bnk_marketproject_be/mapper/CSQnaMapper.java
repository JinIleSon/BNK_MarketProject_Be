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
            @Param("boardType2") String boardType2,
            @Param("boardType3") String boardType3,
            @Param("offset") int offset,
            @Param("limit") int limit);

    int selectTotalCount(
            @Param("userid") String userid,
            @Param("boardType2") String boardType2,
            @Param("boardType3") String boardType3
    );

    // QnA 상세
    CSNoticeDTO selectQnaview(@Param("id") Long id);

    // QnA 댓글 조회
    CSNoticeDTO selectCommentView(@Param("bid") int bid);

    // QnA 등록
    void insertQna(CSNoticeDTO qna);
}
