package kr.co.bnk_marketproject_be;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ActiveProfiles("test")
class BnkMarketProjectBeApplicationTests {

    @MockBean
    private kr.co.bnk_marketproject_be.service.EmailService emailService; // ✅ 추가

    @MockBean
    private kr.co.bnk_marketproject_be.service.SolapiSmsService solapiSmsService; // ✅ 추가

    @Test
    void contextLoads() {
    }
}
