package kr.co.bnk_marketproject_be.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

@Configuration
public class SecurityRoleConfig {

    @Bean
    org.springframework.security.config.core.GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // ROLE_ 접두어 제거
        return new org.springframework.security.config.core.GrantedAuthorityDefaults("");
    }
}
