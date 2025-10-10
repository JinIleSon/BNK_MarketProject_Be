package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.SalesDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseSalesDTO;
import kr.co.bnk_marketproject_be.dto.SalesDTO;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalesService {

    private final AdminMapper adminMapper;

    public PageResponseSalesDTO selectSalesAll(PageRequestDTO pageRequestDTO) {
        // MyBatis 처리
        List<SalesDTO> dtoList = adminMapper.selectSalesAll(pageRequestDTO);

        int total = adminMapper.selectCountTotalSales(pageRequestDTO);

        return PageResponseSalesDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotalSales(PageRequestDTO pageRequestDTO) {
        return adminMapper.selectCountTotalSales(pageRequestDTO);
    }

    public List<SalesDTO> selectSales(){
        return adminMapper.selectSales();
    }
}
