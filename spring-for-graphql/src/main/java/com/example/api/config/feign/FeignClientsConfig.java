package com.example.api.config.feign;

import com.example.api.client.CommentFeignClient;
import com.example.api.client.PostFeignClient;
import com.example.api.client.ProductClient;
import com.example.api.client.UserFeignClient;
import feign.AsyncFeign;
import feign.Logger;
import feign.Request;
import lombok.AllArgsConstructor;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@AllArgsConstructor
public class FeignClientsConfig {
    private final FeignClientProperties feignClientProperties;
    private final FeignClientProperties.FeignClientConfiguration defaultClientConfiguration;

    @Bean
    public ProductClient unstableClient(AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder) {
        return localClient(ProductClient.class, "productFeignClient", defaultAsyncBuilder);
    }

    @Bean
    public CommentFeignClient commentClient(AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder) {
        return remoteClient(CommentFeignClient.class, "commentFeignClient", defaultAsyncBuilder);
    }

    @Bean
    public UserFeignClient userFeignClient(AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder) {
        return remoteClient(UserFeignClient.class, "userFeignClient", defaultAsyncBuilder);
    }

    @Bean
    public PostFeignClient postFeignClient(AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder) {
        return remoteClient(PostFeignClient.class, "postFeignClient", defaultAsyncBuilder);
    }

    private <T> T localClient(Class<T> clientClass, String clientName, AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder) {
        String clientUrl = "http://localhost:8082";
        return defaultAsyncBuilder.options(getClientConfigurationOption(clientName))
                .target(clientClass, clientUrl);
    }

    private <T> T remoteClient(Class<T> clientClass, String clientName, AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder) {
        String clientUrl = "https://jsonplaceholder.typicode.com";
        return defaultAsyncBuilder.options(getClientConfigurationOption(clientName))
                .target(clientClass, clientUrl);
    }

    private Request.Options getClientConfigurationOption(String clientName) {
        final FeignClientProperties.FeignClientConfiguration clientConfig = feignClientProperties.getConfig()
                .getOrDefault(clientName, defaultClientConfiguration);
        clientConfig.setLoggerLevel(Logger.Level.BASIC);

        return new Request.Options(
                clientConfig.getConnectTimeout(), TimeUnit.MILLISECONDS,
                clientConfig.getReadTimeout(), TimeUnit.MILLISECONDS,
                true);
    }
}
