package com.example.api.client;

import com.example.api.model.Product;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

//@Retry(name = ProductClient.UNSTABLE_SERVICE, fallbackMethod = "defaultProduct")
//@FeignClient(name = "productFeignClient", url = "http://localhost:8082")
public interface ProductClient {

    Logger log = LoggerFactory.getLogger(ProductClient.class);

    String UNSTABLE_SERVICE = "unstableService";
    String UNSTABLE_SERVICE_SYNC = "unstableServiceSync";

    @Retry(name = UNSTABLE_SERVICE, fallbackMethod = "defaultProduct")
//    @Retry(name = UNSTABLE_SERVICE)
    @GetMapping("/unstable")
    CompletableFuture<Optional<Product>> unstableWithRetry();

    @Retry(name = UNSTABLE_SERVICE_SYNC, fallbackMethod = "defaultProductSync")
    @GetMapping("/unstable")
    Optional<Product> unstableWithRetrySync();

    default CompletableFuture<Optional<Product>> defaultProduct(Exception ex) {
        log.error("Default product - exception: {}", ex.getMessage());
        Product default_product = new Product("Default Product Async", 12);
        log.warn("Default product: {}", default_product);
        return CompletableFuture.completedFuture(Optional.of(default_product));
    }

    default Optional<Product> defaultProductSync(Exception ex) {
        log.error("Default product - exception: {}", ex.getMessage());
        Product default_product = new Product("Default Product Sync", 12);
        log.warn("Default product: {}", default_product);
        return Optional.of(default_product);
    }
}
