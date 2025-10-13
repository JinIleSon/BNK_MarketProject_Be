package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.AdminBannerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminBannerMapper {
    List<AdminBannerDTO> findAll(@Param("position") String position);
    void insert(AdminBannerDTO dto);
    void delete(Long id);
    void deleteAll(List<Long> ids);

}
