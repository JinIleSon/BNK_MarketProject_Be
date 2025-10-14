package kr.co.bnk_marketproject_be.security;

import kr.co.bnk_marketproject_be.entity.User;
import kr.co.bnk_marketproject_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자가 입력한 아이디로 사용자 조회, 비밀번호에 대한 검증은 이전 컴포넌트인 AuthenticationProvider에서 수행
        User user = userRepository.findByUserId(username);
            if (user == null) {
                log.warn("❌ 사용자 [{}]를 찾을 수 없습니다.", username);
                throw new UsernameNotFoundException("User not found: " + username);
            }
        log.info("✅ 사용자 [{}] 로그인 시도: {}", username, user.getUserId());
        // 인증 객체 생성
        return MyUserDetails.builder()
                .user(user)
                .build();
            //MyUserDetails myUserDetails = MyUserDetails.builder()
            //        .user(user)
            //        .build();

            // 반환되는 인증 객체가 Security Context Holder에 Authentication으로 저장
            //return myUserDetails;

    }
}