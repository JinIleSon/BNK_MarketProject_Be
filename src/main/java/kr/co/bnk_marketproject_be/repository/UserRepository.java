package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.dto.UserDTO;
import kr.co.bnk_marketproject_be.entity.User;
import kr.co.bnk_marketproject_be.mapper.UserMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 사용자 정의 쿼리매서드
    public User findByUserId(String userId);

    boolean existsByUserId(String userId);      // 아이디 중복 확인
    boolean existsByEmail(String email);       // 이메일 중복 확인
    boolean existsByPhone(String phone);       // 전화번호 중복 확인

    public int countByName(String name);
    public int countByEmail(String email);
    public int countByPhone(String phone);

    // 아이디 찾기
    Optional<User> findByNameAndEmail(String name, String email);
    Optional<User> findByNameAndPhone(String name, String phone);

    // 비밀번호 찾기
    Optional<User> findByUserIdAndEmail(String userId, String email);
    Optional<User> findByUserIdAndPhone(String userId, String phone);

    // ✅ 소셜 업서트를 위해 추가
    Optional<User> findByEmail(String email);

    // 아이디찾기시 핸드폰번호(- 하이픈제거) 통과를 위한 추가
    List<User> findByName(String name);

}

