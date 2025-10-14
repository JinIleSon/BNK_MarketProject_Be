package kr.co.bnk_marketproject_be.service.impl;

import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.AdminVersionDTO;
import kr.co.bnk_marketproject_be.mapper.AdminVersionMapper;
import kr.co.bnk_marketproject_be.service.AdminVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminVersionServiceImpl implements AdminVersionService {

    private final AdminVersionMapper versionMapper;

    /** 전체 목록 조회 */
    @Override
    public List<AdminVersionDTO> list() {
        return versionMapper.findAll();
    }

    /** 단건 조회 */
    @Override
    public AdminVersionDTO get(Long id) {
        return versionMapper.findById(id);
    }

    /** 등록 */
    @Transactional
    @Override
    public int add(AdminVersionDTO dto) {
        return versionMapper.insert(dto);
    }

    /** 단일 삭제 */
    @Transactional
    @Override
    public int delete(Long id) {
        return versionMapper.deleteById(id);
    }

    /** 여러 개 삭제 */
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        versionMapper.deleteByIds(ids);
    }
}
