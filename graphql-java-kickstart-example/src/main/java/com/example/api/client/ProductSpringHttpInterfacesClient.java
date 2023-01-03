package com.example.api.client;

import com.example.api.model.Product;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ProductSpringHttpInterfacesClient {

    Logger log = LoggerFactory.getLogger(ProductSpringHttpInterfacesClient.class);

    String UNSTABLE_SERVICE = "unstableServiceHttp";
    String UNSTABLE_SERVICE_SYNC = "unstableServiceHttpSync";
    @GetExchange("/unstable")
    @Retry(name = UNSTABLE_SERVICE, fallbackMethod = "defaultProduct")
    CompletableFuture<Optional<Product>> unstable();

    @Retry(name = UNSTABLE_SERVICE_SYNC, fallbackMethod = "defaultProductSync")
    @GetExchange("/unstableSync")
    Optional<Product> unstableSync();

    default CompletableFuture<Optional<Product>> defaultProduct(Exception ex) {
        ex.printStackTrace();
        log.error("Default product - exception: {}", ex.getMessage());
        log.error("Default product - status code: {}", ((WebClientResponseException)ex).getStatusCode().value());
        log.error("Default product - is5xxServerError: {}", ((WebClientResponseException)ex).getStatusCode().is5xxServerError());
        log.error("Default product - is4xxClientError: {}", ((WebClientResponseException)ex).getStatusCode().is4xxClientError());
        log.error("Default product - status text: {}", ((WebClientResponseException)ex).getStatusText());

        Product default_product = new Product("Default Product Async", 12);
        log.warn("Default product: {}", default_product);
        return CompletableFuture.completedFuture(Optional.of(default_product));
    }

    default Optional<Product> defaultProductSync(Exception ex) {
        ex.printStackTrace();
        log.error("Default product - exception: {}", ex.getMessage());
        Product default_product = new Product("Default Product Sync", 12);
        log.warn("Default product: {}", default_product);
        return Optional.of(default_product);
    }
}
