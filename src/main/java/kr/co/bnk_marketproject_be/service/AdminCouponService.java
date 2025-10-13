package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.CouponsDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminCouponDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseDTO;
import kr.co.bnk_marketproject_be.entity.Coupons;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import kr.co.bnk_marketproject_be.repository.CouponsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminCouponService {
    private final AdminMapper adminMapper;
    private final CouponsRepository couponsRepository;
    private final ModelMapper  modelMapper;

    public PageResponseAdminCouponDTO selectCouponsAll(PageRequestDTO pageRequestDTO) {
        // MyBatis 처리
        List<CouponsDTO> dtoList = adminMapper.selectAllCoupons(pageRequestDTO);

        int total = adminMapper.selectCountTotal(pageRequestDTO);

        return PageResponseAdminCouponDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotal(PageRequestDTO pageRequestDTO) {
        return adminMapper.selectCountTotal(pageRequestDTO);
    }

    public Coupons register(CouponsDTO dto) {

        // 새 쿠폰 생성
        Coupons coupon = Coupons.builder()
                .users_id(dto.getUsers_id())
                .company(dto.getCompany())
                .discount_type(dto.getDiscount_type())
                .coupon_name(dto.getCoupon_name())
                .discount_value(dto.getDiscount_value())
                .valid_from(dto.getValid_from())
                .valid_to(dto.getValid_to())
                .status(dto.getStatus())
                .issuecount(dto.getIssuecount())
                .usercount(dto.getUsercount())
                .note(dto.getNote())
                .build();

        return couponsRepository.save(coupon);
    }
}
