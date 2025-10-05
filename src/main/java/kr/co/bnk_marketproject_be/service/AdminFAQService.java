package kr.co.bnk_marketproject_be.service;

import jakarta.persistence.PrePersist;
import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.entity.AdminFAQ;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import kr.co.bnk_marketproject_be.repository.AdminFAQRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminFAQService {

    private final AdminMapper adminMapper;
    private final AdminFAQRepository  adminFAQRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void increaseHits(int id){
        adminFAQRepository.incrementHits(id);
    }

    public PageResponseAdminFAQDTO selectAllAdminFAQ(PageRequestDTO pageRequestDTO) {
        // MyBatis 처리
        List<AdminFAQDTO> dtoList = adminMapper.selectAllAdminFAQ(pageRequestDTO);

        int total = adminMapper.selectCountTotalAdminFAQ(pageRequestDTO);

        return PageResponseAdminFAQDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotalAdminFAQ(PageRequestDTO pageRequestDTO) {
        return adminMapper.selectCountTotalAdminFAQ(pageRequestDTO);
    }

    // view 찾기용 함수
    public AdminFAQDTO getAdminFAQ(int id){

        Optional<AdminFAQ> optAdminFAQ = adminFAQRepository.findById(id);
        if(optAdminFAQ.isPresent()){
            AdminFAQ adminFAQ = optAdminFAQ.get();
            return modelMapper.map(adminFAQ, AdminFAQDTO.class);
        }
        return null;
    }

    // register용 함수
    public void register(AdminFAQDTO adminFAQDTO){
        adminFAQRepository.save(modelMapper.map(adminFAQDTO, AdminFAQ.class));
    }

    // list - [삭제], view - 삭제 버튼 : delete용 함수
    public void deleteAdminFAQ(int id){
        adminFAQRepository.deleteById(id);
    }

    // modify 수행용 함수
    public void modifyAdminFAQ(AdminFAQDTO adminFAQDTO){
        if(adminFAQRepository.existsById(adminFAQDTO.getId())){
            adminFAQRepository.save(modelMapper.map(adminFAQDTO, AdminFAQ.class));
        }
    }
}
