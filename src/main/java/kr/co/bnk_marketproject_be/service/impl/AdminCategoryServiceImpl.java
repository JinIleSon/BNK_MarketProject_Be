package kr.co.bnk_marketproject_be.service.impl;

import kr.co.bnk_marketproject_be.dto.AdminCategoryDTO;
import kr.co.bnk_marketproject_be.mapper.AdminCategoryMapper;
import kr.co.bnk_marketproject_be.service.AdminCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final AdminCategoryMapper mapper;

    @Override
    public List<AdminCategoryDTO> listAll() {
        return mapper.findAll();
    }

    /** ✅ 1차 카테고리 추가 (parent_id = null) */
    @Transactional
    @Override
    public void addParentCategory(String name) {
        mapper.insert(new AdminCategoryDTO(null, name, null, null)); // category_no 자동
    }

    /** ✅ 2차 카테고리 추가 (parent_id 존재) */
    @Transactional
    @Override
    public void addChildCategory(Integer parentId, String name) {
        mapper.insert(new AdminCategoryDTO(null, name, parentId, null));
    }

    /** ✅ 카테고리 삭제 (자식 포함 + 재정렬) */
    @Transactional
    @Override
    public void deleteCategory(Integer id) {
        // 1️⃣ 삭제 전, parent_id 확보
        AdminCategoryDTO deleted = mapper.findById(id);
        if (deleted == null) return;

        // 2️⃣ 자식 먼저 삭제
        mapper.deleteChildren(id);

        // 3️⃣ 본인 삭제
        mapper.delete(id);

        // 4️⃣ 재정렬 — parentId 있는 경우만 호출 (null 전달 금지)
        if (deleted.getParentId() != null) {
            mapper.reorderAfterDelete(deleted.getParentId());
        }
    }

    /** ✅ 다중 삭제 (각 항목별로 안전하게 처리) */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void deleteAll(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        for (Integer id : ids) {
            deleteCategory(id);
        }
    }

    /** ✅ 전체 업데이트 (부모 먼저 → 자식 나중) */
    @Transactional
    @Override
    public void updateAll(List<AdminCategoryDTO> list) {
        if (list == null || list.isEmpty()) return;

        // 부모 먼저
        list.stream()
                .filter(c -> c.getParentId() == null)
                .forEach(c -> {
                    if (c.getId() == null) mapper.insert(c);
                    else mapper.update(c);
                });

        // 자식 나중에
        list.stream()
                .filter(c -> c.getParentId() != null)
                .forEach(c -> {
                    if (c.getId() == null) mapper.insert(c);
                    else mapper.update(c);
                });
    }

    /** ✅ 특정 부모의 자식만 삭제 (null-safe) */
    @Override
    @Transactional
    public void deleteChildrenOnly(Integer parentId) {
        if (parentId == null) return; // null-safe
        mapper.deleteChildren(parentId);
        mapper.reorderAfterDelete(parentId);
    }
}
