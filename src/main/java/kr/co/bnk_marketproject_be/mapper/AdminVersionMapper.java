package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.AdminVersionDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AdminVersionMapper {
    List<AdminVersionDTO> findAll();
    AdminVersionDTO findById(Long id);
    int insert(AdminVersionDTO dto);
    int deleteById(Long id);
    int deleteByIds(List<Long> ids);
}
