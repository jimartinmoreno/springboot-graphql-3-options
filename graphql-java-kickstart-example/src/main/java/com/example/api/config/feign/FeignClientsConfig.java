package com.example.api.config.feign;

import com.example.api.client.CommentFeignClient;
import com.example.api.client.ProductClient;
import feign.AsyncFeign;
import feign.Logger;
import feign.Request;
import lombok.AllArgsConstructor;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@AllArgsConstructor
public class FeignClientsConfig {
    private final FeignClientProperties feignClientProperties;
    private final FeignClientConfiguration defaultClientConfiguration;

    @Bean
    public ProductClient unstableClient(AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder) {
        return buildLocalhostClient(ProductClient.class, "productFeignClient", defaultAsyncBuilder);
    }

    @Bean
    public CommentFeignClient commentClient(AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder) {
        return buildJsonplaceHolderClient(CommentFeignClient.class, "commentFeignClient", defaultAsyncBuilder);
    }

    private <T> T buildLocalhostClient(Class<T> clientClass, String clientName,
                                       AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder) {
        String clientUrl = "http://localhost:8082";
        return defaultAsyncBuilder.options(getClientConfigurationOption(clientName))
                .target(clientClass, clientUrl);
    }

    private <T> T buildJsonplaceHolderClient(Class<T> clientClass, String clientName,
                                             AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder) {
        String clientUrl = "https://jsonplaceholder.typicode.com";
        return defaultAsyncBuilder.options(getClientConfigurationOption(clientName))
                .target(clientClass, clientUrl);
    }

    private Request.Options getClientConfigurationOption(String clientName) {
        final FeignClientConfiguration clientConfig = feignClientProperties
                .getConfig()
                .getOrDefault(clientName, defaultClientConfiguration);

        clientConfig.setLoggerLevel(Logger.Level.FULL);

        return new Request.Options(
                clientConfig.getConnectTimeout(), TimeUnit.MILLISECONDS,
                clientConfig.getReadTimeout(), TimeUnit.MILLISECONDS,
                true);
    }
}
