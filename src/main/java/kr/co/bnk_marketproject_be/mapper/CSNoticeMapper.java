package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CSNoticeMapper {

    List<CSNoticeDTO> selectNoticeList(@Param("boardType") String boardType,
                                       @Param("offset") int offset,
                                       @Param("pageSize") int pageSize);

    int countNotices(@Param("boardType") String boardType);

    CSNoticeDTO selectNoticeDetail(@Param("id") Long id);
}
