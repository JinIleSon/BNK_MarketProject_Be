package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.entity.AdminMember;
import kr.co.bnk_marketproject_be.entity.User;
import kr.co.bnk_marketproject_be.repository.custom.AdminMemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminMemberRepository extends JpaRepository<AdminMember,Integer>, AdminMemberRepositoryCustom {
    List<AdminMember> findByBoardType(String boardType);
    Optional<User> findByUserId(String user_id);

    Optional<AdminMember> findFirstByUserIdAndBoardType(String userId, String boardType);
}