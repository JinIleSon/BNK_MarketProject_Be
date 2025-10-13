package kr.co.bnk_marketproject_be.service;

import kr.co.bnk_marketproject_be.dto.SessionDataDTO;
import kr.co.bnk_marketproject_be.dto.UserDTO;
import kr.co.bnk_marketproject_be.entity.User;
import kr.co.bnk_marketproject_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;  // JPA
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SessionDataDTO sessionData;


    //private final UserMybatisRepository mybatisRepository; // MyBatis



    public void save(UserDTO userDTO) {
        // 비밀번호 암호화
        String encodedPass = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPass);

        // DTO를 Entity로 변환
        User user = modelMapper.map(userDTO, User.class);

        userRepository.save(user);
    }

    public boolean existsByUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public UserDTO getUser(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * 서버사이드 회원가입 처리: 세션 인증 확인, 중복 체크, 비밀번호 암호화, DB 저장
     */
    public void register(UserDTO userDTO) {
        // 1) 서버 세션에서 이메일/휴대폰 인증 확인
        // 이메일 인증만 체크
        if (sessionData == null || !sessionData.isVerified()) {
            throw new IllegalStateException("이메일 인증을 완료해주세요.");
        }
        //추후 번호까지 될때 쓰는 거
        //if (sessionData == null || !sessionData.isVerified() || !sessionData.isSmsVerified()) {
        //    throw new IllegalStateException("이메일 인증을 완료해주세요.");
        //}

        // 휴대폰 인증안되도 되게 하려고 추가한것
        // 2) 휴대폰은 선택사항 — 전화번호가 들어온 경우만 SMS 인증 체크
        // 여기 아해 한줄은 살려도 되는데 일단 다 없애고 해봄
        //if (userDTO.getPhone() != null && !userDTO.getPhone().trim().isEmpty()) {
        //    if (!Boolean.TRUE.equals(sessionData.isSmsVerified())) {
        //        throw new IllegalStateException("휴대폰 인증을 완료해주세요.");
        //    }
        //}


        // 3) 중복 검사 - Repository 메서드 이름에 맞춰 사용하세요
        // 예: existsByUserid / existsByEmail / existsByPhone 등
        if (userRepository.existsByUserId(userDTO.getUserId())) {
            throw new IllegalStateException("이미 사용중인 아이디입니다.");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalStateException("이미 사용중인 이메일입니다.");
        }
        //if (userDTO.getPhone() != null && userRepository.existsByPhone(userDTO.getPhone())) {
        //    throw new IllegalStateException("이미 사용중인 휴대폰 번호입니다.");
        //}
        if (userDTO.getPhone() != null && !userDTO.getPhone().trim().isEmpty()) {
            if (userRepository.existsByPhone(userDTO.getPhone())) {
                throw new IllegalStateException("이미 사용중인 휴대폰 번호입니다.");
            }
        }

        // 4) 비밀번호 암호화 및 DTO->Entity 변환 저장
        String encoded = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encoded);

        // ✅ 5) role 값 보정
        if (userDTO.getRole() == null || userDTO.getRole().isBlank()) {
            // 프론트에서 role 미전달 시 기본값 설정
            userDTO.setRole("user");
        } else {
            // 대소문자 통일 + 허용된 값만 필터
            String roleLower = userDTO.getRole().toLowerCase();
            if (!List.of("user", "seller", "admin").contains(roleLower)) {
                userDTO.setRole("user");
            } else {
                userDTO.setRole(roleLower);
            }
        }

        // 6) DTO -> Entity 변환
        User user = modelMapper.map(userDTO, User.class);
        // 필요하면 기본 role("MEMBER") 설정
        // user.setRole("ROLE_USER");// 5) DTO -> Entity 변환
        // Oracle 제약조건 위반의 원인임.

        // 이 형태가 위반이 안됨.
        user.setRole(userDTO.getRole());

        // 7) 저장
        userRepository.save(user);

        // 8) 선택사항: 세션 인증 상태 초기화
        sessionData.setVerified(false);
        sessionData.setSmsVerified(false);
    }



    public List<UserDTO> getUserAll(){
        return null;
    }
    public void modify(UserDTO userDTO){}
    public void remove(String userId){}

    public int countUser(String type, String value){

        int count = 0;

        if(type.equals("user_id")){
            //count = userRepository.countByUser_id(value);
        }else if(type.equals("name")){
            count = userRepository.countByName(value);
        }else if(type.equals("email")){
            count = userRepository.countByEmail(value);

            if(count == 0){
                // 인증코드 이메일 전송
                emailService.sendCode(value);
            }

        }else if(type.equals("phone")){
            count = userRepository.countByPhone(value);
        }
        return count;
    }


    public Optional<UserDTO> findUserId(String name, String method, String email, String phone) {
        Optional<User> userOpt;
        // 판매자 브랜드명까지 검색되게 조건 확장
        if ("email".equalsIgnoreCase(method)) {
            userOpt = userRepository.findByNameAndEmail(name, email);
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findByNameAndEmail(name.toUpperCase(), email);
            }
        } else {
            userOpt = userRepository.findByNameAndPhone(name, phone);
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findByNameAndPhone(name.toUpperCase(), phone);
            }
        }

        return userOpt.map(user -> modelMapper.map(user, UserDTO.class));
    }

    // 아이디 + 이메일로 사용자 검증
    public boolean verifyUserForPasswordReset(String userId, String email, String phone) {
        Optional<User> userOpt = Optional.empty();
        if (email != null && !email.isBlank()) {
            userOpt = userRepository.findByUserIdAndEmail(userId, email);
        } else if (phone != null && !phone.isBlank()) {
            userOpt = userRepository.findByUserIdAndPhone(userId, phone);
        }
        return userOpt.isPresent();
    }

    // 새 비밀번호 저장
    public void resetPassword(String userId, String newPassword) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("비밀번호 재설정 대상 userId={}, role={}", user.getUserId(), user.getRole());

    }


}
