package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.CSNoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface CSFaqMapper {

    List<CSNoticeDTO> selectFaqList(Map<String, Object> params);
    List<CSNoticeDTO> selectFaqListByType(Map<String, Object> params);
    CSNoticeDTO selectFaqView(Long id);
}

