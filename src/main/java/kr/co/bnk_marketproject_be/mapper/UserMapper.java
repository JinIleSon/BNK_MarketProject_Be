package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;


// user에서는 안씀, seller할때 써볼예정 일단은 그대로 놔둠

@Mapper
public interface UserMapper {

    void insertUser(UserDTO user);
}

