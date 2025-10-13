// src/main/java/kr/co/bnk_marketproject_be/security/DevAutoLoginConfig.java
package kr.co.bnk_marketproject_be.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
//@Profile("local") // 로컬에서만 쓰려면 주석 해제
public class DevAutoLoginConfig {

    @Bean
    public OncePerRequestFilter devAutoLoginFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                    throws ServletException, IOException {

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    String uid = request.getParameter("uid"); // ?uid= 값 지원
                    if (uid == null) uid = "1";              // 기본 1 (개발용)
                    var auth = new UsernamePasswordAuthenticationToken(
                            uid, "N/A", List.of(new SimpleGrantedAuthority("ROLE_USER")));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                chain.doFilter(request, response);
            }
        };
    }
}
