package kr.co.bnk_marketproject_be.service;

import jakarta.transaction.Transactional;
import kr.co.bnk_marketproject_be.dto.*;
import kr.co.bnk_marketproject_be.mapper.AdminMapper;
import kr.co.bnk_marketproject_be.mapper.MyPageMapper;
import kr.co.bnk_marketproject_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {
    // 푸시용
    private final MyPageMapper myPageMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO selectUser(String user_id){
        return myPageMapper.selectUser(user_id);
    }

    public boolean verifyPassword(String userId, String rawPassword){
        String encodedFromDb = userRepository.findByUserId(userId).getPassword();
        return passwordEncoder.matches(rawPassword, encodedFromDb);
    }

    @Transactional
    public void updateContact(String userId, UserDTO dto){
        var user = userRepository.findByUserId(userId);
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPostcode(dto.getPostcode());
        user.setAddress(dto.getAddress());
        user.setDetailAddress(dto.getDetailAddress());
        // JPA 더티체킹으로 업데이트 반영
    }

    @Transactional
    public void withdrawUser(String userId){
        var user = userRepository.findByUserId(userId);
        // 원하는 방식대로 비식별화/파기
        user.setEmail(null);
        // 비밀번호도 무력화
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setName(null);
        user.setPhone(null);
        user.setPostcode(null);
        user.setAddress(null);
        user.setDetailAddress(null);
        // 상태 필드가 있으면 "WITHDRAWN" 같은 값으로 표시 권장
        user.setRole("withdrawn"); // 필요시(탈퇴)
        user.setCreated_at(null);
        user.setUpdated_at(null);
        user.setGender(null);
        user.setBirth(null);
        user.setProvider(null);
        user.setProviderId(null);


    }
    // 마이페이지/포인트내역

    public PageResponseUserPointDTO selectUserPoint(PageRequestDTO pageRequestDTO, String userId) {
        // MyBatis 처리
        List<AdminPointDTO> dtoList = myPageMapper.selectUserPoint(pageRequestDTO, userId);

        int total = myPageMapper.selectCountTotalUserPoint(pageRequestDTO, userId);

        return PageResponseUserPointDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotalUserPoint(PageRequestDTO pageRequestDTO, String userId) {
        return myPageMapper.selectCountTotalUserPoint(pageRequestDTO, userId);
    }

    // 마이페이지/쿠폰

    public PageResponseUserCouponsNowDTO selectUserCouponsNow(PageRequestDTO pageRequestDTO, String userId) {
        // MyBatis 처리
        List<MyPageCouponsNowDTO> dtoList = myPageMapper.selectUserCouponsNow(pageRequestDTO, userId);

        int total = myPageMapper.selectCountTotalUserCouponsNow(pageRequestDTO, userId);

        return PageResponseUserCouponsNowDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotalUserCouponsNow(PageRequestDTO pageRequestDTO, String userId) {
        return myPageMapper.selectCountTotalUserCouponsNow(pageRequestDTO, userId);
    }

    // 마이페이지/문의하기

    public PageResponseAdminInquiryDTO selectAllInquiry(PageRequestDTO pageRequestDTO, String userId) {
        // MyBatis 처리
        List<AdminInquiryDTO> dtoList = myPageMapper.selectAllInquiry(pageRequestDTO, userId);

        int total = myPageMapper.selectCountTotalInquiry(pageRequestDTO, userId);

        return PageResponseAdminInquiryDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotalInquiry(PageRequestDTO pageRequestDTO, String userId) {
        return myPageMapper.selectCountTotalInquiry(pageRequestDTO, userId);
    }
}
