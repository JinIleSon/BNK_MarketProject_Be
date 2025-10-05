package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.AdminInquiry;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminInquiryRepository extends JpaRepository<AdminInquiry, Long> {

    // 최근 5건 (CREATED_AT이 문자열이면 PK 역순을 권장)
    List<AdminInquiry> findByBoardTypeOrderByIdDesc(String boardType, Pageable pageable);

    // 목록 페이징용(필요시)
    List<AdminInquiry> findByBoardTypeOrderByIdDesc(String boardType);

    // 손진일 추가 - 클릭 당 조회수 1 증가
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update AdminFAQ a set a.hits = a.hits + 1 where a.id = :id")
    int incrementHits(@Param("id") Long id);

    // look을 1로 만들어 답변완료 상태로 만듦
    @Modifying
    @Query("UPDATE AdminInquiry a SET a.look = 1 WHERE a.id = :id")
    void updateLookToOne(@Param("id") Long id);
}
