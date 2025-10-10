package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CSFaqMapper {

    List<CSNoticeDTO> selectFaqList();

    CSNoticeDTO selectFaqDetail(@Param("id") Long id);

    void insertFaq(CSNoticeDTO faq);
}
