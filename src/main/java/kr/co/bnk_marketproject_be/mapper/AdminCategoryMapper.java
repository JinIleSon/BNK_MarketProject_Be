package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.AdminCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminCategoryMapper {
    List<AdminCategoryDTO> findAll();
    AdminCategoryDTO findById(Integer id);  // ✅ 추가
    void insert(AdminCategoryDTO dto);
    void update(AdminCategoryDTO dto);
    void delete(Integer id);
    void deleteChildren(Integer id);
    void reorderAfterDelete(Integer parentId);
}
