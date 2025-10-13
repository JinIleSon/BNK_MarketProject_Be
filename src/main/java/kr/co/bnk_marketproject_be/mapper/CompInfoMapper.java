package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.CompInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CompInfoMapper {

    public List<CompInfoDTO> selectAll();
    public List<CompInfoDTO> selectFive();

}
