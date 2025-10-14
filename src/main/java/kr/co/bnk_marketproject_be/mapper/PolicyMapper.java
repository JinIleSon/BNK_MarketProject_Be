package kr.co.bnk_marketproject_be.mapper;

import kr.co.bnk_marketproject_be.dto.PolicyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PolicyMapper {

    public PolicyDTO selectAllPolicies();
}
