package kr.co.bnk_marketproject_be.service;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.dto.AdminMemberDTO;
import kr.co.bnk_marketproject_be.entity.AdminMember;
import kr.co.bnk_marketproject_be.entity.AdminMember;
import kr.co.bnk_marketproject_be.entity.User;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import kr.co.bnk_marketproject_be.repository.AdminMemberRepository;
import kr.co.bnk_marketproject_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminMemberService {

    private final AdminMemberRepository adminMemberRepository;
    private final UserRepository  userRepository;

    private final AdminMapper adminMapper;
    private final ModelMapper modelMapper;

    public PageResponseAdminMemberDTO findAdminMemberAll(PageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Tuple> pageTuple = null;
        if(pageRequestDTO.getSearchType() != null){
            // 검색 글 검색
            pageTuple = adminMemberRepository.selectAdminMemberAllForSearch(pageRequestDTO, pageable);
        }else{
            // 일반 글 목록
            pageTuple = adminMemberRepository.selectAdminMemberAllForList(pageRequestDTO, pageable);
        }

        log.info("pageTuple={}",pageTuple);
        List<Tuple> tupleList = pageTuple.getContent();
        int total = (int)pageTuple.getTotalElements();

        List<AdminMemberDTO> dtoList = tupleList.stream()
                .map(tuple -> {
                    AdminMember adminMember = tuple.get(0, AdminMember.class);
                    String name = tuple.get(1, String.class);
                    String userId = tuple.get(2, String.class);
                    String email = tuple.get(3, String.class);
                    String phone = tuple.get(4, String.class);
                    String address = tuple.get(5,String.class);
                    String role = tuple.get(6, String.class);

                    adminMember.setName(name);
                    adminMember.setUserId(userId);
                    adminMember.setEmail(email);
                    adminMember.setPhone(phone);
                    adminMember.setAddress(address);
                    adminMember.setRole(role);


                    return modelMapper.map(adminMember, AdminMemberDTO.class);
                })
                .toList();

        return PageResponseAdminMemberDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public AdminMemberDTO selectMember(String user_id){
        return adminMapper.selectMember(user_id);
    }

    @Transactional
    public void modifyMember(AdminMemberDTO adminMemberDTO){
        if (adminMemberRepository.existsById(adminMemberDTO.getId())) {
            AdminMember adminMember = adminMemberRepository.findById(adminMemberDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다." + adminMemberDTO.getId()));
            // AdminMember(BOARD) - 허용한 것만 덮기
            if (adminMemberDTO.getName() != null)        adminMember.setRep(adminMemberDTO.getName());
            if (adminMemberDTO.getGender() != null)     adminMember.setGender(adminMemberDTO.getGender());
            if (adminMemberDTO.getGrade() != null)      adminMember.setGrade(adminMemberDTO.getGrade());
            if (adminMemberDTO.getTel() != null)        adminMember.setTel(adminMemberDTO.getTel());
            if (adminMemberDTO.getLook() != null)       adminMember.setLook(adminMemberDTO.getLook());
            if (adminMemberDTO.getContent() != null)    adminMember.setContent(adminMember.getContent());
            // point 등은 폼에서 안 받으면 건드리지 않음
            User user = userRepository.findByUserId(adminMemberDTO.getUserId());
            user.setUserId(adminMemberDTO.getUserId());
            user.setName(adminMemberDTO.getName());
            user.setGender(adminMemberDTO.getGender());
            user.setEmail(adminMemberDTO.getEmail());
            user.setPhone(adminMemberDTO.getTel());
            user.setPostcode(adminMemberDTO.getZipcode());
            user.setAddress(adminMemberDTO.getAddress1());
            user.setDetailAddress(adminMemberDTO.getAddress2());
            user.setCreated_at(adminMemberDTO.getCreated_at());
            if(adminMemberDTO.getUpdated_at() != null) user.setUpdated_at(adminMemberDTO.getUpdated_at());
        }
    }

    @Transactional
    public void updateOneMember(String userId){
        adminMapper.updateMember(userId);
    }

    @Transactional
    public void bulkUpdateGrades(List<MemberGradeUpdateDTO> updates) {
        for (MemberGradeUpdateDTO u : updates) {
            adminMemberRepository.findFirstByUserIdAndBoardType(u.getUserId(), "memberlist")
                    .ifPresent(am -> {
                        am.setGrade(u.getGrade());
                    });
        }
    }
}