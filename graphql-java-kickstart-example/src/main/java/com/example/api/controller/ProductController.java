package com.example.api.controller;

import com.example.api.model.Product;
import com.example.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    CompletableFuture<Optional<Product>> getProduct() {
        return productService.getProduct();
    }

    @GetMapping("/productSync")
    Optional<Product> getProductSync() {
        Optional<Product> productSync = productService.getProductSync();
        log.info("product: {}", productSync);
        return productSync;
    }
}
