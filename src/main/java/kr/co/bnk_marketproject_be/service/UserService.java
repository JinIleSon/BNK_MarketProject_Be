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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;
import java.util.UUID;
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

        if (userDTO.getBirth() != null) {
            user.setBirth(userDTO.getBirth());
        }

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

    // 📪 구글 로그인을 위한
    @Transactional
    public User upsertGoogleUser(String email, Map<String, Object> attrs) {
        if (email == null || email.isBlank()) {
            // 드물게 이메일 비공개 계정일 수 있으니 방어
            throw new IllegalStateException("Google 프로필에 email이 없습니다.");
        }

        // 1) 기존 유저 있으면 업데이트
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isPresent()) {
            User user = opt.get();

            // 이름/프로필 등 보정(필요하면)
            Object name = attrs.get("name");
            if ((user.getName() == null || user.getName().isBlank()) && name instanceof String) {
                user.setName((String) name);
            }
            Object picture = attrs.get("picture");
            if (picture instanceof String) {
                // 엔티티에 맞는 필드명으로 바꿔줘 (예: setProfileImage / setAvatarUrl 등)
                // user.setProfileImage((String) picture);
            }

            // provider/role 보정
            user.setProvider("google");
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("user");
            }

            return userRepository.save(user);
        }

        // 2) 없으면 신규 생성
        User user = new User();
        user.setEmail(email);

        // userId 생성: 이메일 앞부분을 기본으로, 중복이면 숫자 붙이기
        String base = baseFromEmail(email);
        user.setUserId(makeUniqueUserId(base));

        Object name = attrs.get("name");
        if (name instanceof String) user.setName((String) name);

        Object picture = attrs.get("picture");
        if (picture instanceof String) {
            // user.setProfileImage((String) picture);
        }

        user.setProvider("google");
        user.setRole("user");

        // 비밀번호 제약(널 금지)이 있으면 랜덤값 인코딩
        String randomPwd = "oauth2:" + UUID.randomUUID();
        user.setPassword(passwordEncoder.encode(randomPwd));

        // 필요하면 활성화 상태 등 기본값도 세팅
        // user.setEnabled(true);

        return userRepository.save(user);
    }

    private String baseFromEmail(String email) {
        int at = email.indexOf('@');
        return (at > 0) ? email.substring(0, at) : email;
    }

    private String makeUniqueUserId(String base) {
        String candidate = base;
        int i = 1;
        while (userRepository.existsByUserId(candidate)) {
            candidate = base + i;
            i++;
        }
        return candidate;
    }

    // 📪 kakao 로그인 - 특성상 빈email이 많아서 예외처리
    @Transactional
    public User upsertKakaoUser(String email, Map<String, Object> attrs) {
        // 1️⃣ 이메일 없을 경우 임시 이메일 생성
        if (email == null || email.isBlank()) {
            Object kakaoId = attrs.get("id");
            email = "kakao_" + (kakaoId != null ? kakaoId : UUID.randomUUID()) + "@kakao-temp.com";
        }

        // 2️⃣ 람다 안에서 쓸 수 있도록 final 변수로 복사
        final String safeEmail = email;

        // 3️⃣ 이제 safeEmail을 사용
        Optional<User> opt = userRepository.findByEmail(safeEmail);
        User user = opt.orElseGet(() -> {
            User u = new User();
            u.setEmail(safeEmail);
            u.setUserId(makeUniqueUserId(baseFromEmail(safeEmail)));
            u.setPassword(passwordEncoder.encode("oauth2:" + UUID.randomUUID()));
            return u;
        });

        // 4️⃣ 프로필 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attrs.get("kakao_account");
        if (kakaoAccount != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null && profile.get("nickname") != null) {
                user.setName((String) profile.get("nickname"));
            }

            // 이메일이 뒤늦게라도 있으면 업데이트
            if (kakaoAccount.get("email") != null && safeEmail.endsWith("@kakao-temp.com")) {
                user.setEmail((String) kakaoAccount.get("email"));
            }
        }

        user.setProvider("kakao");
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("user");
        }

        return userRepository.save(user);
    }


    // 📪 공통 OAuth 로그인 (Google / Kakao / Naver 통합)
    @Transactional
    public User upsertOAuthUser(String provider, String email, Map<String, Object> attrs) {
        // ✅ 1️⃣ 이메일이 없으면 임시 이메일 생성
        if (email == null || email.isBlank()) {
            Object id = attrs.get("id");
            email = provider + "_" + (id != null ? id : UUID.randomUUID()) + "@oauth-temp.com";
        }

        // ✅ 2️⃣ 람다에서 쓸 안전한 final 변수
        final String safeEmail = email;

        // ✅ 3️⃣ findByEmail 및 생성
        Optional<User> opt = userRepository.findByEmail(safeEmail);
        User user = opt.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(safeEmail);
            newUser.setUserId(makeUniqueUserId(baseFromEmail(safeEmail)));
            newUser.setPassword(passwordEncoder.encode("oauth2:" + UUID.randomUUID()));
            return newUser;
        });

        String name = null;
        String picture = null;

        switch (provider) {
            case "google":
                name = (String) attrs.get("name");
                picture = (String) attrs.get("picture");
                break;

            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attrs.get("kakao_account");
                if (kakaoAccount != null) {
                    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                    if (profile != null) {
                        name = (String) profile.get("nickname");
                        picture = (String) profile.get("profile_image_url");
                    }
                    // ✅ safeEmail을 사용하여 이메일 갱신
                    if (kakaoAccount.get("email") != null && safeEmail.endsWith("@oauth-temp.com")) {
                        user.setEmail((String) kakaoAccount.get("email"));
                    }
                }
                break;

            case "naver":
                Map<String, Object> response = (Map<String, Object>) attrs.get("response");
                if (response != null) {
                    name = (String) response.get("name");
                    picture = (String) response.get("profile_image");
                    if (response.get("email") != null && safeEmail.endsWith("@oauth-temp.com")) {
                        user.setEmail((String) response.get("email"));
                    }
                }
                break;
        }

        if (name != null && (user.getName() == null || user.getName().isBlank())) {
            user.setName(name);
        }

        user.setProvider(provider);
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("user");
        }

        return userRepository.save(user);
    }




}
