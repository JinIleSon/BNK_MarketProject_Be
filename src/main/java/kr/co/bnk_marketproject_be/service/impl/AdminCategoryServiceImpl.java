package kr.co.bnk_marketproject_be.service.impl;

import kr.co.bnk_marketproject_be.dto.AdminCategoryDTO;
import kr.co.bnk_marketproject_be.mapper.AdminCategoryMapper;
import kr.co.bnk_marketproject_be.service.AdminCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
// 👇 스프링 트랜잭션으로 교체
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

    @Transactional
    @Override
    public void addParentCategory(String name) {
        Integer nextNo = mapper.nextOrderForParent(null);
        mapper.insert(new AdminCategoryDTO(null, name, null, nextNo));
    }

    @Transactional
    @Override
    public void addChildCategory(Integer parentId, String name) {
        Integer nextNo = mapper.nextOrderForParent(parentId);
        mapper.insert(new AdminCategoryDTO(null, name, parentId, nextNo));
    }

    @Transactional
    @Override
    public void deleteCategory(Integer id) {
        mapper.deleteChildren(id);
        mapper.delete(id);
    }

    // ✅ 커밋에서 먼저 실행되는 삭제(별도 트랜잭션으로 확정 커밋)
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAll(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        for (Integer id : ids) {
            mapper.deleteChildren(id);
            mapper.delete(id);
        }
    }

    @Transactional
    @Override
    public void updateAll(List<AdminCategoryDTO> list) {
        if (list == null || list.isEmpty()) return;

        // (선택) 간단 검증
        for (AdminCategoryDTO dto : list) {
            if (dto.getName() == null || dto.getName().isBlank()) {
                throw new IllegalArgumentException("Category name is required");
            }
            if (dto.getCategoryNo() == null) {
                dto.setCategoryNo(1);
            }
        }

        // 1) 1차(부모: parentId == null) 먼저
        list.stream()
                .filter(c -> c.getParentId() == null)
                .forEach(c -> {
                    if (c.getId() == null) mapper.insert(c);  // 신규 → INSERT
                    else mapper.update(c);                     // 기존 → UPDATE
                });

        // 2) 2차(자식: parentId != null) 다음
        list.stream()
                .filter(c -> c.getParentId() != null)
                .forEach(c -> {
                    if (c.getId() == null) mapper.insert(c);
                    else mapper.update(c);
                });
    }

    @Override
    @Transactional
    public void deleteChildrenOnly(Integer parentId) {
        mapper.deleteChildren(parentId);
    }



}
