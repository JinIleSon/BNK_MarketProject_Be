package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.AdminVersionDTO;
import java.util.List;

public interface AdminVersionService {

    /** 전체 목록 조회 */
    List<AdminVersionDTO> list();

    /** 단건 조회 */
    AdminVersionDTO get(Long id);

    /** 등록 */
    int add(AdminVersionDTO dto);

    /** 단일 삭제 */
    int delete(Long id);

    /** 여러 개 삭제 (선택 삭제용) */
    void delete(List<Long> ids);
}
