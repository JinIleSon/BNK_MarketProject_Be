package kr.co.bnk_marketproject_be.support;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration(proxyBeanMethods = false)
public class TestBeansConfig {

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        // ✅ 실제 메일 전송하지 않도록 Mockito 목으로 대체
        return Mockito.mock(JavaMailSender.class);
    }
}
