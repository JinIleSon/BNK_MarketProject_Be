package kr.co.bnk_marketproject_be.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// 아래 두개 DB 데이터 로그인을 위한 것, import 수동
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailsService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationProvider customAuthenticationProvider) throws Exception {

        // ✅ DB 기반 인증 (CustomAuthenticationProvider)
        http.authenticationProvider(customAuthenticationProvider);

        // ✅ 로그인 설정
        http.formLogin(form -> form
                .loginPage("/member/login")            // 로그인 페이지
                .loginProcessingUrl("/member/login")   // 로그인 요청 처리 URL (form action과 동일)
                .defaultSuccessUrl("/main/main/page", true) // 로그인 성공 시
                // 로그인 실패 성공 시 핸들러
                .failureHandler((request, response, ex) -> {
                    String reason = "unknown";
                    if (ex instanceof org.springframework.security.authentication.BadCredentialsException) {
                        reason = "bad";           // 아이디/비밀번호 불일치
                    } else if (ex instanceof org.springframework.security.authentication.LockedException) {
                        reason = "locked";        // 계정 잠김
                    } else if (ex instanceof org.springframework.security.authentication.DisabledException) {
                        reason = "disabled";      // 비활성/미인증
                    } else if (ex instanceof org.springframework.security.authentication.CredentialsExpiredException) {
                        reason = "expired";       // 비밀번호 만료
                    }
                    // 필요시: 로그인 시도 아이디 로깅
                    String ctx = request.getContextPath();        // "/NICHIYA"
                    System.out.println("❌ 로그인 실패: userId=" + request.getParameter("userId") + ", reason=" + reason);
                    response.sendRedirect(ctx + "/member/login?error=" + reason);
                })
                .successHandler((request, response, authentication) -> {
                    String ctx = request.getContextPath();
                    String username = authentication.getName();
                    System.out.println("✅ 로그인 성공: 아이디=" + username);
                    response.sendRedirect(ctx + "/main/main/page");
                })
                .usernameParameter("userId")
                .passwordParameter("password")
                .permitAll()
        );

        // ✅ 로그아웃 설정
        http.logout(logout -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/member/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        );

        // ✅ 접근 권한 설정
        http.authorizeHttpRequests(auth -> auth

                .requestMatchers(
                        "/member/issue-temp-password",   // ⬅ 임시 비번 발급 API
                        "/NICHIYA/email/**"             // ⬅ 이메일 인증 전송/검증
                ).permitAll()

                // 🔹 정적 리소스 및 공개 페이지는 누구나 접근 가능
                .requestMatchers(
                        "/", "/index",
                        "/css/**", "/js/**", "/images/**", "/fonts/**",
                        "/favicon.ico","/NICHIYA/favicon.ico", "/error",
                        "/oauth2/**", "/login/oauth2/**", "/NICHIYA/login/oauth2/**",
                        "/user/**",
                        "/email/**",
                        "/member/**",
                        "/seller/**",
                        "/policy/**",
                        "/compinfo/**",
                        "/main/**",
                        "/product/**",
                        "/cs/**",
                        "/member/**",
                        "/mypage/**",
                        "/api/sms/**"  // 여기 이것 추가!!!!
                ).permitAll()

                // 🔹 일반 회원, 셀러 접근 허용
                .requestMatchers("/article/**").hasAnyRole("user", "seller", "admin")
                .requestMatchers("/mypage/**").hasAnyRole("user", "seller", "admin")
                .requestMatchers("/admin/**").hasAnyRole( "admin")
                .requestMatchers("/api/mypage/**").hasAnyRole("user", "seller", "admin")

                // 🔹 관리자(admin)는 모든 페이지 접근 가능
                .anyRequest().hasAnyRole("admin")
        );

        // ✅ CSRF (쿠키 기반) 너무 복잡하고 어려워서 안함
//        http.csrf(csrf -> csrf
//                //.ignoringRequestMatchers("/member/login")
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//        );

        http.csrf(csrf -> csrf.disable());

        http.oauth2Login(oauth -> oauth
                // 소셜 로그인도 같은 커스텀 로그인 페이지를 쓰고 싶다면(선택)
                .loginPage("/member/login")

                .successHandler((request, response, authentication) -> {
                    String ctx = request.getContextPath();
                    String email = authentication.getName(); // 보통 이메일
                    org.slf4j.LoggerFactory.getLogger("OAuth2")
                            .info("OAuth2 authentication success: user={}", email);
                    // 컨텍스트(/NICHIYA)는 자동으로 붙으니 앞에 쓰지 말고 루트부터!
                    response.sendRedirect(ctx + "/main/main/page");  // ← ctx 붙이기
                })
                .failureHandler((request, response, ex) -> {
                    String ctx = request.getContextPath();
                    org.slf4j.LoggerFactory.getLogger("OAuth2")
                            .warn("OAuth2 authentication failed: {}", ex.getMessage());
                    response.sendRedirect(ctx + "/member/login?error=social"); // ← ctx 붙이기
                })
        );



        // ✅ remember-me (자동 로그인)
        http.rememberMe(remember -> remember
                .key("NICHIYA-REMEMBER-ME")
                .tokenValiditySeconds(60 * 60 * 24 * 7) // 7일 유지
                .userDetailsService(myUserDetailsService)
        );

        return http.build();
    }

    // ✅ 비밀번호 암호화기 (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ 인증 매니저 (AuthenticationManager)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 🚫 개발용 가짜 로그인 (InMemoryUserDetailsManager)
    // - 현재는 DB 연동 로그인으로 전환 예정이므로 주석 처리
    /*
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("a") // 아이디
                .password(passwordEncoder.encode("123")) // 비밀번호
                .roles("USER") // 권한
                .build();
        return new InMemoryUserDetailsManager(user);
    }
    */
}
