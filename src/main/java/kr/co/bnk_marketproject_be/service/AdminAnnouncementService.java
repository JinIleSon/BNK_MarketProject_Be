package kr.co.bnk_marketproject_be.service;

import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.AdminAnnouncementDTO;
import kr.co.bnk_marketproject_be.dto.PageRequestDTO;
import kr.co.bnk_marketproject_be.dto.PageResponseAdminAnnouncementDTO;
import kr.co.bnk_marketproject_be.entity.AdminAnnouncement;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import kr.co.bnk_marketproject_be.repository.AdminAnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminAnnouncementService {

    private final AdminMapper adminMapper;
    private final AdminAnnouncementRepository  adminAnnouncementRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void increaseHits(int id){
        adminAnnouncementRepository.incrementHits(id);
    }

    public PageResponseAdminAnnouncementDTO selectAllAdminAnnouncement(PageRequestDTO pageRequestDTO) {
        // MyBatis 처리
        List<AdminAnnouncementDTO> dtoList = adminMapper.selectAllAdminAnnouncement(pageRequestDTO);

        int total = adminMapper.selectCountTotalAdminAnnouncement(pageRequestDTO);

        return PageResponseAdminAnnouncementDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotal(PageRequestDTO pageRequestDTO) {
        return adminMapper.selectCountTotalAdminAnnouncement(pageRequestDTO);
    }

    // view 찾기용 함수
    public AdminAnnouncementDTO getAdminAnnouncement(int id){

        Optional<AdminAnnouncement> optAdminAnnouncement = adminAnnouncementRepository.findById(id);
        if(optAdminAnnouncement.isPresent()){
            AdminAnnouncement adminAnnouncement = optAdminAnnouncement.get();
            return modelMapper.map(adminAnnouncement, AdminAnnouncementDTO.class);
        }
        return null;
    }

    // register용 함수
    public void register(AdminAnnouncementDTO adminAnnouncementDTO){
        adminAnnouncementRepository.save(modelMapper.map(adminAnnouncementDTO, AdminAnnouncement.class));
    }

    // list - [삭제], view - 삭제 버튼 : delete용 함수
    public void deleteAdminAnnouncement(int id){
        adminAnnouncementRepository.deleteById(id);
    }

    // modify 수행용 함수
    public void modifyAdminAnnouncement(AdminAnnouncementDTO adminAnnouncementDTO){
        if(adminAnnouncementRepository.existsById(adminAnnouncementDTO.getId())){
            adminAnnouncementRepository.save(modelMapper.map(adminAnnouncementDTO, AdminAnnouncement.class));
        }
    }
}
