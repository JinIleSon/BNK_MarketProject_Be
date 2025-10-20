package kr.co.bnk_marketproject_be;

import kr.co.bnk_marketproject_be.support.TestBeansConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestBeansConfig.class)   // ✅ 목 빈 주입
class BnkMarketProjectBeApplicationTests {
    @Test
    void contextLoads() {
    }
}
