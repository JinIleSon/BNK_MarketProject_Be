package kr.co.bnk_marketproject_be.repository;

import kr.co.bnk_marketproject_be.dto.UserDTO;
import kr.co.bnk_marketproject_be.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserMybatisRepository {

    private final UserMapper userMapper;

    public void insertUser(UserDTO dto) {
        userMapper.insertUser(dto);
    }
}
