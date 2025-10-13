package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.AdminBannerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminBannerService {

    // position별 조회 (position 없으면 전체)
    List<AdminBannerDTO> findAll(String position);

    // 신규 저장
    void save(AdminBannerDTO dto, MultipartFile file);

    // 단일 삭제
    void delete(Long id);

    // 복수 삭제
    void deleteAll(List<Long> ids);
}
