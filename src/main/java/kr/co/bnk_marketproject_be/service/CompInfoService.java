package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.CompInfoDTO;
import kr.co.bnk_marketproject_be.mapper.CompInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompInfoService {

    private final CompInfoMapper compInfoMapper;

    // 소식과 이야기 모두 출력
    public List<CompInfoDTO> selectAllCompInfo(){
        return compInfoMapper.selectAll();
    }

    // 메인에 5개만. 소식과 이야기
    public List<CompInfoDTO> selectFiveCompInfo(){
        return compInfoMapper.selectFive();
    }

}
