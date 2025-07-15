package com.back;

import com.back.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest
class MyBoardBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
