package com.example.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;
import java.util.function.Consumer;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
@Slf4j
public class SpringForGraphqlApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(SpringForGraphqlApplication.class, args);
        Map<String, String> env = System.getenv();
        env.forEach((k, v) -> log.info("{}: {}", k, v));
    }

    @Bean
    @ConditionalOnProperty(
            value = "spring.cloud.stream.bindings.authenticationMessageConsumer-in-0.enabled",
            havingValue = "true")
    public Boolean eventConsumer(@Value("${spring.cloud.stream.bindings.authenticationMessageConsumer-in-0.enabled}") boolean kafkaEnabled) {
        log.info("eventConsumer - kafkaEnabled: {}", kafkaEnabled);
        return kafkaEnabled;
    }

}
