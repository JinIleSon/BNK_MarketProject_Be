package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.AdminSiteConfigDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminSiteConfigMapper {
    AdminSiteConfigDTO selectOne();
    int insert(AdminSiteConfigDTO dto);
    int update(AdminSiteConfigDTO dto);
    List<AdminSiteConfigDTO> findAll();
    int deleteByIds(@Param("ids") List<Long> ids);
}