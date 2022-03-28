package com.fromlabs.inventory.apisecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = ApiSecurityApplication.class)
class ApiSecurityApplicationTests {

    @Test
    void contextLoads() {
    }

}
