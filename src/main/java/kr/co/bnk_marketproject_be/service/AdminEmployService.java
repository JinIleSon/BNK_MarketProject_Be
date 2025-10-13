package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.AdminEmployDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminEmployDTO;
import kr.co.bnk_marketproject_be.entity.AdminEmploy;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import kr.co.bnk_marketproject_be.repository.AdminEmployRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminEmployService {
    private final AdminMapper adminMapper;
    private final AdminEmployRepository  adminEmployRepository;
    private final ModelMapper  modelMapper;

    public PageResponseAdminEmployDTO selectAdminEmployAll(PageRequestDTO pageRequestDTO) {
        // MyBatis 처리
        List<AdminEmployDTO> dtoList = adminMapper.selectAllAdminEmploy(pageRequestDTO);

        int total = adminMapper.selectCountTotalAdminEmploy(pageRequestDTO);

        return PageResponseAdminEmployDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotal(PageRequestDTO pageRequestDTO) {
        return adminMapper.selectCountTotalAdminEmploy(pageRequestDTO);
    }

    // 선택삭제 구현
    public void delete(int id){
        adminMapper.deleteAdminEmploy(id);
    };

    // 채용등록(register)
    public void registerEmploy(AdminEmployDTO adminEmployDTO){
        adminEmployRepository.save(modelMapper.map(adminEmployDTO, AdminEmploy.class));
    }

    // 선택삭제 2트
    public void deleteEmploys(List<Integer> ids){
        adminEmployRepository.deleteAllById(ids);
    }
}
