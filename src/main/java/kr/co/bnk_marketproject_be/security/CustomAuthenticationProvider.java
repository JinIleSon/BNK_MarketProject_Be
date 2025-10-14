// BCrypt 실패 시(DB에 있는 초반 데이터 인퓨즈한 회원들) 인증 로직
// 스프링 security 인증로직이 암호화되지않은 PW를 무조건 거부하기 때문에
// 인증 실패시를 따로 만들어서 강제 로그인
// 프젝을 위한 로직, 실제로는 이렇게 하면 안됨

package kr.co.bnk_marketproject_be.security;

import kr.co.bnk_marketproject_be.entity.User;
import kr.co.bnk_marketproject_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        log.info("🔐 로그인 시도: 아이디={}, 비밀번호={}", username, rawPassword);

        MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
        User user = userDetails.getUser();

        String dbPassword = user.getPassword();

        boolean matches = false;

        // 1️⃣ BCrypt 비교
        if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$")) {
            matches = passwordEncoder.matches(rawPassword, dbPassword);
        }
        // 2️⃣ 평문 비교 fallback
        else {
            matches = rawPassword.equals(dbPassword);
        }

        if (!matches) {
            log.warn("❌ 로그인 실패: 아이디={}, 비밀번호 불일치", username);
            throw new BadCredentialsException("❌ 비밀번호 불일치");
        }

        log.info("✅ 로그인 성공: 아이디={} (권한={})", username, user.getRole());

        // 관리자면 ROLE_ADMIN 부여
        if ("admin".equalsIgnoreCase(username)) {
            userDetails.getUser().setRole("ROLE_ADMIN");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}