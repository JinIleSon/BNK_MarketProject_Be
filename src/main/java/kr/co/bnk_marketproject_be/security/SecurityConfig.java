package kr.co.bnk_marketproject_be.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        // 로그인 설정
//        http.formLogin(form -> form
//                .loginPage("/user/login")
//                .defaultSuccessUrl("/")
//                .failureUrl("/user/login?error=true")
//                .usernameParameter("usid")
//                .passwordParameter("pass")
//        );
//
//        // 로그아웃 설정
//        http.logout(logout -> logout
//                .logoutUrl("/user/logout")
//                .invalidateHttpSession(true)
//                .logoutSuccessUrl("/user/login?logout=true"));
//
//        // 인가 설정
//        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
//                .requestMatchers("/member/**").hasAnyRole("ADMIN", "MANAGER", "MEMBER")
//                .requestMatchers("/guest/**").permitAll()
//                .requestMatchers("/article/**").hasAnyRole("ADMIN", "MANAGER", "MEMBER")
//                .anyRequest().permitAll()
//        );
//
//        // 기타 설정
//        http.csrf(CsrfConfigurer::disable);

        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/api/**",
                                "/h2-console/**",
                                "/actuator/**"
                        )
                )
                .formLogin(form -> form.permitAll())
                .logout(logout -> logout.permitAll());

        return http.build();
    }

    // 실제 배포때는 없앨까요? 만들기 쉽게 user/123만 하면 로그인되게 했어요.
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("a")               // 아이디
                .password(passwordEncoder.encode("123")) // 비밀번호 고정
                .roles("USER")                  // 권한
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}