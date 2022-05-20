package com.example.demo;

import com.example.demo.config.CorsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private CorsConfig corsConfig;

    @Test
    void should_test_cors_configuration() {
        assertTrue(corsConfig.getLogs()
                .contains("\"/api/user\" path registered for CORS configuration."));
    }
}