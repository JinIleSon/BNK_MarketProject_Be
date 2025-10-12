package kr.co.bnk_marketproject_be.service;

import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.AdminCommentDTO;
import kr.co.bnk_marketproject_be.dto.AdminInquiryDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminInquiryDTO;
import kr.co.bnk_marketproject_be.entity.AdminComment;
import kr.co.bnk_marketproject_be.entity.AdminInquiry;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import kr.co.bnk_marketproject_be.repository.AdminInquiryRepository;
import kr.co.bnk_marketproject_be.repository.AdminCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminInquiryNoticeService {

    private final AdminMapper adminMapper;
    private final AdminInquiryRepository  adminInquiryRepository;
    private final AdminCommentRepository adminCommentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void increaseHits(Long id){
        adminInquiryRepository.incrementHits(id);
    }

    public PageResponseAdminInquiryDTO selectAllAdminInquiry(PageRequestDTO pageRequestDTO) {
        // MyBatis 처리
        List<AdminInquiryDTO> dtoList = adminMapper.selectAllAdminInquiry(pageRequestDTO);

        int total = adminMapper.selectCountTotalAdminInquiry(pageRequestDTO);

        return PageResponseAdminInquiryDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotalAdminInquiry(PageRequestDTO pageRequestDTO) {
        return adminMapper.selectCountTotalAdminInquiry(pageRequestDTO);
    }

    // view 찾기용 함수
    public AdminInquiryDTO getAdminInquiry(Long id){

        Optional<AdminInquiry> optAdminInquiry = adminInquiryRepository.findById(id);
        if(optAdminInquiry.isPresent()){
            AdminInquiry adminInquiry = optAdminInquiry.get();
            return modelMapper.map(adminInquiry, AdminInquiryDTO.class);
        }
        return null;
    }

    // register용 함수
    public void register(AdminInquiryDTO adminInquiryDTO){
        adminInquiryRepository.save(modelMapper.map(adminInquiryDTO, AdminInquiry.class));
    }

    // list - [삭제], view - 삭제 버튼 : delete용 함수
    public void deleteAdminInquiry(Long id){
        adminInquiryRepository.deleteById(id);
    }

    // modify 수행용 함수
    public void modifyAdminInquiry(AdminInquiryDTO adminInquiryDTO){
        if(adminInquiryRepository.existsById(adminInquiryDTO.getId())){
            adminInquiryRepository.save(modelMapper.map(adminInquiryDTO, AdminInquiry.class));
        }
    }

    // Comment 등록용 함수
    @Transactional // 데이터 변경 시 필요한 애노테이션
    public void registerInquiryComment(AdminCommentDTO adminCommentDTO){
        adminCommentRepository.save(modelMapper.map(adminCommentDTO, AdminComment.class));
        adminInquiryRepository.updateLookToOne(adminCommentDTO.getBid());
    };

    // Comment 찾기용 함수
    @Transactional
    public AdminCommentDTO getAdminComment(Long bid){
        return adminCommentRepository.findTopByBidOrderByCidDesc(bid)
                .map(c -> modelMapper.map(c, AdminCommentDTO.class))
                .orElse(null);
    }

    // 선택삭제
    @Transactional
    public void deleteInquirys(List<Long> ids){
        adminInquiryRepository.deleteAllById(ids);
    }
}
