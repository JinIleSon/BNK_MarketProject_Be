package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyPageMapper {

    // 마이페이지/나의설정 - 손진일 추가
    public UserDTO selectUser(@Param("user_id") String user_id);
}
