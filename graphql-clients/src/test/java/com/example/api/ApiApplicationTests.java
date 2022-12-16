package com.example.api;

import com.example.api.webclient.MyGraphQLWebClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiApplicationTests {

    @Autowired
    MyGraphQLWebClient myGraphQLWebClient;

    @Test
    void loadContext() {
        assertThat(myGraphQLWebClient).isNotNull();
    }
}