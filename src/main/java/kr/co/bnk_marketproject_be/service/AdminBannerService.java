package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.AdminBannerDTO;
import kr.co.bnk_marketproject_be.entity.AdminBanner;
import kr.co.bnk_marketproject_be.entity.AdminBanner.BannerPosition;
import kr.co.bnk_marketproject_be.entity.AdminBanner.BannerStatus;
import kr.co.bnk_marketproject_be.repository.AdminBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminBannerService {

    private final AdminBannerRepository repo;

    /** 등록 */
    public AdminBannerDTO create(AdminBannerDTO f) {
        LocalDate from = LocalDate.from(f.getDateFrom());
        LocalDate to   = LocalDate.from(f.getDateTo());
        LocalTime tf   = f.getTimeFrom();   // LocalTime or null
        LocalTime tt   = f.getTimeTo();

        LocalDateTime vFrom = (from == null) ? null
                : LocalDateTime.of(from, (tf == null) ? LocalTime.MIN : tf);

        LocalDateTime vTo = (to == null) ? null
                : LocalDateTime.of(to, (tt == null) ? LocalTime.of(23, 59, 59) : tt);

        if (vFrom != null && vTo != null && vFrom.isAfter(vTo)) {
            throw new IllegalArgumentException("시작이 종료보다 늦습니다.");
        }

        // 2) 엔티티 생성
        AdminBanner b = new AdminBanner();
        b.setName(f.getName());
        b.setSize(f.getSize());
        b.setBgColor(f.getBgColor());
        b.setLinkUrl(emptyIfNull(f.getLinkUrl()));
        b.setPosition(f.getPosition());
        b.setVisibleFrom(vFrom);
        b.setVisibleTo(vTo);
        b.setStatus(f.getStatus() != null ? f.getStatus() : BannerStatus.INACTIVE);
        b.setCreatedAt(LocalDateTime.now());
        b.setUpdatedAt(null); // 초기 null 허용

        // 3) 1차 저장 (ID 확보)
        b = repo.save(b);

        // 4) 파일이 있으면 저장 + 경로 업데이트
        if (f.getFile() != null && !f.getFile().isEmpty()) {
            String path = saveImage(f.getFile(), b.getId());
            b.setImagePath(path);
            b = repo.save(b);
        }

        return toDto(b);
    }

    /** 탭별 목록 */
    @Transactional(readOnly = true)
    public List<AdminBannerDTO> listByPosition(String pos) {
        BannerPosition p = BannerPosition.valueOf(pos);
        return repo.findByPositionOrderByIdDesc(p).stream()
                .map(this::toDto)
                .toList();
    }

    /** 선택 삭제 */
    public void deleteByIds(List<Long> ids) {
        repo.deleteAllById(ids);
    }

    /** 상태 변경 (활성/비활성) */
    public AdminBannerDTO toggleStatus(Long id, boolean enable) {
        AdminBanner b = repo.findById(id).orElseThrow();
        b.setStatus(enable ? BannerStatus.ACTIVE : BannerStatus.INACTIVE);
        b.setUpdatedAt(LocalDateTime.now());
        return toDto(b);
    }



    // ===== util =====
    private String emptyIfNull(String s) { return (s == null) ? "" : s; }

    private LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s); // yyyy-MM-dd 형식
        } catch (Exception e) {
            throw new IllegalArgumentException("날짜 형식은 yyyy-MM-dd 이어야 합니다.");
        }
    }



    @Value("${app.upload.dir:/var/nichiya/uploads}")
    private String uploadRoot;

    private String saveImage(MultipartFile file, Long groupId) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("배너 이미지가 비어있습니다.");
        }

        String original = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot != -1) ext = original.substring(dot);

        String filename = java.util.UUID.randomUUID() + (ext.isEmpty() ? ".bin" : ext);

        Path targetDir  = java.nio.file.Paths.get(uploadRoot, "banners", String.valueOf(groupId));
        Path targetPath = targetDir.resolve(filename);

        try {
            java.nio.file.Files.createDirectories(targetDir); // 상위 폴더 보장

            try (var in = file.getInputStream()) {
                java.nio.file.Files.copy(in, targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

            // 브라우저에서 접근할 공개 URL (WebConfig에서 /uploads/** 매핑)
            return "/uploads/banners/" + groupId + "/" + filename;

        } catch (java.io.IOException e) {
            throw new RuntimeException("배너 이미지 저장 실패", e);
        }
    }


    private AdminBannerDTO toDto(AdminBanner b) {

        return AdminBannerDTO.builder()
                .id(b.getId())
                .name(b.getName())
                .size(b.getSize())
                .bgColor(b.getBgColor())
                .linkUrl(b.getLinkUrl())
                .position(b.getPosition() != null ? b.getPosition() : null)
                .dateFrom(b.getVisibleFrom() != null ? b.getVisibleFrom().toLocalDate() : null)
                .dateTo(b.getVisibleTo() != null ? b.getVisibleTo().toLocalDate() : null)
                .timeFrom(b.getVisibleFrom() != null ? b.getVisibleFrom().toLocalTime() : null)
                .timeTo(b.getVisibleTo() != null ? b.getVisibleTo().toLocalTime() : null)
                .imagePath(b.getImagePath())
                .status(b.getStatus())
                .createdAt(b.getCreatedAt())
                .updatedAt(b.getUpdatedAt())
                .build();
    }


}
