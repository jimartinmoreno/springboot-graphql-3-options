package com.example.api.service;

import com.example.api.client.ProductClient;
import com.example.api.client.ProductSpringHttpInterfacesClient;
import com.example.api.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductClient unstableClient;

    private final ProductSpringHttpInterfacesClient productSpringHttpInterfacesClient;

    @Override
    public CompletableFuture<Optional<Product>> getProduct() {
//        CompletableFuture<Optional<Product>> product = productSpringHttpInterfacesClient.unstable();
        CompletableFuture<Optional<Product>> product = unstableClient.unstableWithRetry();
        return product;
    }


    @Override
    public Optional<Product> getProductSync() {
//        Optional<Product> product = productSpringHttpInterfacesClient.unstableSync();
        Optional<Product> product = unstableClient.unstableWithRetrySync();
        return product;
    }
}
