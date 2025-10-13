package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.AdminNotice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminNoticeRepository extends JpaRepository<AdminNotice, Long> {

    List<AdminNotice> findAllByOrderByIdDesc(Pageable pageable);
    // 날짜 문자열 정렬이 가능하면 아래도 추가로
    List<AdminNotice> findAllByOrderByCreatedAtDesc(Pageable pageable);
}

