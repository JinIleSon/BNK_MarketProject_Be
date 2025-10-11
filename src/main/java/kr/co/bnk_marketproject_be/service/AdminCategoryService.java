package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.AdminCategoryDTO;

import java.util.List;

public interface AdminCategoryService {
    List<AdminCategoryDTO> listAll();
    void addParentCategory(String name);
    void addChildCategory(Integer parentId, String name);
    void deleteCategory(Integer id);        // 1차면 children도 같이
    void updateAll(List<AdminCategoryDTO> list); // 이름/순서 일괄 커밋
    void deleteChildrenOnly(Integer parentId);
    // ✅ 커밋에서 쓰는 삭제 메서드 추가
    void deleteAll(List<Integer> ids);

}
