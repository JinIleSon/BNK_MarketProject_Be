package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.AdminPoint;
import kr.co.bnk_marketproject_be.repository.custom.AdminPointRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminPointRepository extends JpaRepository<AdminPoint,Integer>, AdminPointRepositoryCustom {
    List<AdminPoint> findByBoardType(String boardType);
}