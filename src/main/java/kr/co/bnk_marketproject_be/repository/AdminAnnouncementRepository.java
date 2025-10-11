package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.AdminAnnouncement;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAnnouncementRepository extends JpaRepository<AdminAnnouncement, Integer> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update AdminAnnouncement a set a.hits = a.hits + 1 where a.id = :id")
    int incrementHits(@Param("id") int id);
}
