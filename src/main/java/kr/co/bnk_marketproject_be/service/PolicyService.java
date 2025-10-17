package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.PolicyDTO;
import kr.co.bnk_marketproject_be.mapper.PolicyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyMapper policyMapper;
    public PolicyDTO selectAllPolicies() {
        return policyMapper.selectAllPolicies();
    }
}
