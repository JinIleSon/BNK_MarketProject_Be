package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.AdminCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminCategoryMapper {

    // 서비스에서 쓰는 시그니처와 정확히 일치
    List<AdminCategoryDTO> findAll();

    int insert(AdminCategoryDTO dto);
    int update(AdminCategoryDTO dto);

    int delete(@Param("id") Integer id);
    int deleteChildren(@Param("id") Integer id);

    // parentId가 null일 수도 있으니 @Param 이름 고정
    Integer nextOrderForParent(@Param("parentId") Integer parentId);
}
