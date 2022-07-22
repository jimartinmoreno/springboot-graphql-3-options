package com.example.api.graphql.resovers.product;

import com.example.api.model.Product;
import com.example.api.service.ProductService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ProductQueryResolver implements GraphQLQueryResolver {

    private final ProductService productService;

    Optional<Product> getProductSync() {
        return productService.getProductSync();
    }

    CompletableFuture<Optional<Product>> getProduct() {
        return productService.getProduct();
    }
}
