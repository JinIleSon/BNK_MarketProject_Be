package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.AdminSiteConfigDTO;
import java.util.List;

public interface AdminSiteConfigService {

    AdminSiteConfigDTO get();
    int upsert(AdminSiteConfigDTO dto);
    void update(AdminSiteConfigDTO dto);

    List<AdminSiteConfigDTO> list();          // 히스토리 목록 (최신순)
    void add(AdminSiteConfigDTO dto);         // 새 버전 행 INSERT (author, changeLog 포함)
    void delete(List<Long> ids);              // 선택삭제
}
