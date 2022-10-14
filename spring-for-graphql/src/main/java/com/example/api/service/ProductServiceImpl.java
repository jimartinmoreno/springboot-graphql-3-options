package com.example.api.service;

import com.example.api.client.ProductClient;
import com.example.api.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductClient unstableClient;

    @Override
    public CompletableFuture<Optional<Product>> getProduct() {
        return unstableClient.unstableWithRetry();
    }

    @Override
    public Optional<Product> getProductSync() {
        return unstableClient.unstableWithRetrySync();
    }
}
