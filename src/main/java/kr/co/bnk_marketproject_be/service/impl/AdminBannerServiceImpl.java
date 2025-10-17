package kr.co.bnk_marketproject_be.service.impl;

import kr.co.bnk_marketproject_be.dto.AdminBannerDTO;
import kr.co.bnk_marketproject_be.mapper.AdminBannerMapper;
import kr.co.bnk_marketproject_be.service.AdminBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminBannerServiceImpl implements AdminBannerService {

    private final AdminBannerMapper adminBannerMapper;


    @Override
    public List<AdminBannerDTO> findAll(String position) {
        if (position == null || position.isBlank()) {
            return adminBannerMapper.findAll(null);
        }
        return adminBannerMapper.findAll(position);
    }

    @Override
    public void save(AdminBannerDTO dto, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String ext = getFileExtension(file.getOriginalFilename());
                String fileName = UUID.randomUUID() + ext;

                // ✅ 절대경로로 안전하게 지정
                String baseDir = System.getProperty("user.dir") + "/upload/banners";
                Path uploadPath = Paths.get(baseDir);

                // ✅ 경로 생성
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    log.info("📁 업로드 폴더 생성: {}", uploadPath);
                }

                // ✅ 파일 저장
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());
                log.info("✅ 파일 저장 완료: {}", filePath);

                // ✅ DB 저장용 절대 URL
                dto.setImagePath("/upload/banners/" + fileName);
            }

            dto.setCreatedAt(LocalDateTime.now());
            dto.setStatus("ACTIVE");
            adminBannerMapper.insert(dto);
            log.info("배너 저장 완료: {}", dto.getName());
        } catch (IOException e) {
            log.error("❌ 배너 저장 중 파일 오류 발생", e);
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
        }
    }


    @Override
    public void delete(Long id) {
        adminBannerMapper.delete(id);
        log.info("배너 삭제 완료: id={}", id);
    }

    @Override
    public void deleteAll(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        adminBannerMapper.deleteAll(ids);
        log.info("배너 {}개 삭제 완료", ids.size());
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) return "";
        int idx = fileName.lastIndexOf('.');
        return (idx > 0) ? fileName.substring(idx) : "";
    }
}
