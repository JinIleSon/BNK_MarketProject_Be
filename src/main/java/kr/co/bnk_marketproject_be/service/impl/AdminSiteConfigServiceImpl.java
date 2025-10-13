package kr.co.bnk_marketproject_be.service.impl;

import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.AdminSiteConfigDTO;
import kr.co.bnk_marketproject_be.mapper.AdminSiteConfigMapper;
import kr.co.bnk_marketproject_be.service.AdminSiteConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSiteConfigServiceImpl implements AdminSiteConfigService {

    private final AdminSiteConfigMapper siteConfigMapper;

    @Override
    public AdminSiteConfigDTO get() {
        return siteConfigMapper.selectOne();
    }

    @Transactional
    @Override
    public int upsert(AdminSiteConfigDTO dto) {
        AdminSiteConfigDTO current = siteConfigMapper.selectOne();
        if (current == null) {
            return siteConfigMapper.insert(dto); // 최초 생성
        }
        return siteConfigMapper.update(dto);     // 갱신
    }

    @Transactional
    @Override
    public void update(AdminSiteConfigDTO dto) {
        siteConfigMapper.update(dto);
    }

    @Override
    public List<AdminSiteConfigDTO> list() {
        return siteConfigMapper.findAll();          // ★ mapper → siteConfigMapper 로 통일
    }

    @Transactional
    @Override
    public void add(AdminSiteConfigDTO dto) {
        siteConfigMapper.insert(dto);               // ★ 통일
    }

    @Transactional
    @Override
    public void delete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        siteConfigMapper.deleteByIds(ids);          // ★ 통일
    }
}
