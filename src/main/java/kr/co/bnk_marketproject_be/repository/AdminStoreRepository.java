package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.AdminStore;
import kr.co.bnk_marketproject_be.repository.custom.AdminStoreRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminStoreRepository extends JpaRepository<AdminStore,Integer>, AdminStoreRepositoryCustom {
    List<AdminStore> findByBoardType(String boardType);
}