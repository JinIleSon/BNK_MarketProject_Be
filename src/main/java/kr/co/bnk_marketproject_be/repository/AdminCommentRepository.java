package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.AdminComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminCommentRepository extends JpaRepository<AdminComment, Long> {
    Optional<AdminComment> findTopByBidOrderByCidDesc(Long bid); // 최신 1건
}
