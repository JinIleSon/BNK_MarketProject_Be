package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.SellerDTO;
import kr.co.bnk_marketproject_be.dto.UserDTO;
import kr.co.bnk_marketproject_be.entity.User;
import kr.co.bnk_marketproject_be.repository.SellerMybatisRepository;
import kr.co.bnk_marketproject_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SellerService {

    private final SellerMybatisRepository sellerRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final UserRepository userRepo; // user JPA Repository

    // 판매자 회원가입
    public void register(SellerDTO dto) {
        log.info("판매자 회원가입 처리 시작: {}", dto.getSellerId());

        // 무결성 위배 조건때문에 user에서 쓰고 있는 아이디, 이메일은 쓰지 못하게 검사해야됨.
        // 아이디 중복 검사
        if (userRepo.existsByUserId(dto.getSellerId()) || existsBySellerId(dto.getSellerId())) {
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }

        // 이메일 중복 검사
        if (userRepo.existsByEmail(dto.getEmail()) || existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // name이 비어있으면 brand_name으로 대체 (Oracle null 방지)
        if (dto.getName() == null || dto.getName().isBlank()) {
            dto.setName(dto.getBrand_name());
        }

        // created_at 기본값 설정 (null 방지)
        if (dto.getCreated_at() == null) {
            dto.setCreated_at(java.time.LocalDateTime.now().toString());
        }

        // 비밀번호 암호화
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        //  판매자는 user 테이블에도 저장해야 함
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(dto.getSellerId());
        userDTO.setPassword(dto.getPassword());
        userDTO.setName(dto.getBrand_name()); // 이름 대신 브랜드명 저장
        userDTO.setEmail(dto.getEmail());     // SellerDTO에 이메일있어야 null 제약뒤배안됨
        userDTO.setPhone(dto.getPhone());
        userDTO.setRole("seller");            // 중요! seller로 등록

        // 주소 정보 추가, seller테이블에는 필요없지만 user에 들어갈때 null로 되서 추가
        userDTO.setAddress(dto.getAddress());
        userDTO.setDetailAddress(dto.getDetailAddress());
        userDTO.setPostcode(dto.getPostcode());

        //  DTO → Entity로 변환
        User userEntity = modelMapper.map(userDTO, User.class);

        // JPA 방식으로 저장 (insert) user는 jpa방식이라서
        userRepo.save(userEntity);

        // user PK 가져와서 SellerDTO에 넣기
        dto.setUserId(userEntity.getId());


        //  MyBatis insert 호출, 판매자 테이블에도 저장, seller는 mybatis라 이렇게 저장
        try {
            sellerRepo.insertSeller(dto);
            log.info("판매자 회원가입 성공: {}", dto.getSellerId());
        } catch (Exception e) {
            log.error("판매자 등록 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("판매자 등록 중 오류 발생", e);

        }

        // 위랑 중복이라서 위의 코드로 해보고 안되면 다시 여기로 회귀.
        //sellerRepo.insertSeller(dto);


    }

    public boolean existsBySellerId(String sellerId) {
        return sellerRepo.countBySellerId(sellerId) > 0;
    }

    public boolean existsByEmail(String email) {
        return sellerRepo.countByEmail(email) > 0;
    }

    public boolean existsByPhone(String phone) {
        return sellerRepo.countByPhone(phone) > 0;
    }

    // 아이디 찾기 (이름 + 이메일/전화)
    public Optional<SellerDTO> findSellerId(String name, String method, String email, String phone) {
        SellerDTO seller;
        if ("email".equalsIgnoreCase(method)) {
            seller = sellerRepo.findByNameAndEmail(name, email);
        } else {
            String clean = phone == null ? null : phone.replaceAll("\\D", "");
            seller = sellerRepo.findByNameAndPhone(name, clean);
        }
        return Optional.ofNullable(seller);
    }

    // 비밀번호 재설정 전 인증
    public boolean verifySellerForPasswordReset(String sellerId, String email, String phone) {
        if (email != null && !email.isBlank()) {
            return sellerRepo.findBySellerIdAndEmail(sellerId, email) != null;
        }

        if (phone != null && !phone.isBlank()) {
            String cleanPhone = phone.replaceAll("\\D", ""); // 숫자만 남김
            return sellerRepo.findBySellerIdAndPhone(sellerId, cleanPhone) != null;
        }

        return false;
    }

    // 비밀번호 변경
    public void resetPassword(String sellerId, String newPassword) {
        User user = userRepo.findByUserId(sellerId); // 판매자는 userId = sellerId 로 저장되어 있음
        if (user == null) {
            throw new IllegalArgumentException("해당 sellerId의 사용자(USERS)를 찾을 수 없습니다.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    public String issueTempPasswordAndReturnPlain(String sellerId) {
        String temp = generateTempPassword(10);
        resetPassword(sellerId, temp);  // resetPassword 안에서 이미 암호화 처리
        return temp;
    }

    private String generateTempPassword(int len) {
        final String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
        java.security.SecureRandom r = new java.security.SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(chars.charAt(r.nextInt(chars.length())));
        return sb.toString();
    }
}
