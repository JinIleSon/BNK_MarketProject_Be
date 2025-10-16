package kr.co.bnk_marketproject_be.security;

import kr.co.bnk_marketproject_be.service.CustomOAuth2UserService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           CustomAuthenticationProvider customAuthenticationProvider,
                                           CustomOAuth2UserService customOAuth2UserService) throws Exception {

        // ✅ DB 기반 인증 (CustomAuthenticationProvider)
        http.authenticationProvider(customAuthenticationProvider);

        // ✅ 로그인 설정
        http.formLogin(form -> form
                .loginPage("/member/login")            // 로그인 페이지
                .loginProcessingUrl("/member/login")   // 로그인 요청 처리 URL (form action과 동일)
                .defaultSuccessUrl("/NICHIYA/main/main/page", true) // 로그인 성공 시
                // 로그인 실패 성공 시 핸들러
                .failureHandler((request, response, exception) -> {
                    String username = request.getParameter("userId");
                    System.out.println("❌ 로그인 실패 (Controller 로그): 아이디=" + username);
                    exception.printStackTrace();
                    response.sendRedirect("/member/login?error=true");
                })
                .successHandler((request, response, authentication) -> {
                    String username = authentication.getName();
                    System.out.println("✅ 로그인 성공 (Controller 로그): 아이디=" + username);
                    response.sendRedirect("/NICHIYA/main/main/page");
                })
                //.failureUrl("/member/login?error=true")    // 실패 시
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

                // 접근 권한 변경 시 이 순서대로 안하면 스프링 자체 run 오류납니다!
                // 1) 완전 공개(정적/공용)
                .requestMatchers(
                        "/", "/index",
                        "/css/**", "/js/**", "/images/**", "/fonts/**",
                        "/favicon.ico", "/error"
                ).permitAll()

                // 2) OAuth2 엔드포인트 공개
                .requestMatchers("/oauth2/**", "/login/oauth2/**",
                        "/oauth2/authorization/**", "/auth/login/kakao/**").permitAll()

                // 3) 사이트 공개 페이지
                .requestMatchers(
                        "/user/**",
                        "/email/**",
                        "/member/**",
                        "/seller/**",
                        "/policy/**",
                        "/compinfo/**",
                        "/main/**",
                        "/product/**",
                        "/cs/**"
                ).permitAll()

                // 4) 인증/권한 필요한 구간 (구체 → 덜 구체 순서)
                // 🔹 일반 회원, 셀러 접근 허용
                .requestMatchers("/article/**").hasAnyAuthority("user", "seller", "admin")
                .requestMatchers("/mypage/**").hasAnyAuthority("user", "seller", "admin")
                .requestMatchers("/admin/**").hasAnyAuthority( "admin")

                // 5) 마지막에 anyRequest
                // 🔹 관리자(admin)는 모든 페이지 접근 가능
                .anyRequest().hasAnyAuthority("admin")
                //.anyRequest().authenticated()
        );

        // 구글 로그인
        // ✅ OAuth2 로그인 활성화 (필수)
        http.oauth2Login(oauth -> oauth
                .loginPage("/member/login") // 로그인 페이지 재사용
                .userInfoEndpoint(u -> u.userService(customOAuth2UserService)) // 사용자 정보 매핑
                .successHandler(oAuth2LoginSuccessHandler) // ✅ 성공 시 핸들러 실행
                .failureHandler((req, res, ex) -> { // ✅ 실패 시 에러 로그 확인
                    ex.printStackTrace();
                    res.sendRedirect("/member/login?error=true");
                })
        );


        // ✅ CSRF (쿠키 기반) 너무 복잡하고 어려워서 안함
//        http.csrf(csrf -> csrf
//                //.ignoringRequestMatchers("/member/login")
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//        );

        http.csrf(csrf -> csrf.disable());


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
