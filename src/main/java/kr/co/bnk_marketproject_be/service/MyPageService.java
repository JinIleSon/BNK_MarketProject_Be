package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.UserDTO;
import kr.co.bnk_marketproject_be.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {

    private final MyPageMapper myPageMapper;

    public UserDTO selectUser(String user_id){
        return myPageMapper.selectUser(user_id);
    }
}
