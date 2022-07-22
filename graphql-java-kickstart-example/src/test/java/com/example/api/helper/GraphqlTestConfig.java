package com.example.api.helper;

import com.example.api.config.async.ExecutorFactory;
import com.example.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class GraphqlTestConfig {

    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }

    public Executor myExecutor() {
        return mock(Executor.class);
    }

    @Bean
    public GraphqlTestHelper graphqlTestHelper(GraphQLTestTemplate graphQLTestTemplate, ObjectMapper objectMapper) {
        return new GraphqlTestHelper(graphQLTestTemplate, objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

}
