package com.example.api.graphql.controllers.product;

import com.example.api.model.Product;
import com.example.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductQueryController {

    private final ProductService productService;

    @QueryMapping
    Optional<Product> getProductSync() {
        return productService.getProductSync();
    }

    @QueryMapping
    CompletableFuture<Optional<Product>> getProduct() {
        return productService.getProduct();
    }
}
