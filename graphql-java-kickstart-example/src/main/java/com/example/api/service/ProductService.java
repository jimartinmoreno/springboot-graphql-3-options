package com.example.api.service;

import com.example.api.model.Product;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ProductService {

    CompletableFuture<Optional<Product>> getProduct();

    Optional<Product> getProductSync();
}
