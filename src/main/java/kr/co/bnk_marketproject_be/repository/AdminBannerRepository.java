package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.AdminBanner;
import kr.co.bnk_marketproject_be.entity.AdminBanner.BannerPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminBannerRepository extends JpaRepository<AdminBanner, Long> {
    List<AdminBanner> findByPositionOrderByIdDesc(BannerPosition position);
}
