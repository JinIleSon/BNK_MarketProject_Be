package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.CouponsNowDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminCouponDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminCouponsNowDTO;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminCouponsNowService {
    private final AdminMapper adminMapper;

    public PageResponseAdminCouponsNowDTO selectCouponsNowAll(PageRequestDTO pageRequestDTO) {
        // MyBatis 처리
        List<CouponsNowDTO> dtoList = adminMapper.selectAllCouponsNow(pageRequestDTO);

        int total = adminMapper.selectCountTotalCouponsNow(pageRequestDTO);

        return PageResponseAdminCouponsNowDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotal(PageRequestDTO pageRequestDTO) {
        return adminMapper.selectCountTotalCouponsNow(pageRequestDTO);
    }
}
