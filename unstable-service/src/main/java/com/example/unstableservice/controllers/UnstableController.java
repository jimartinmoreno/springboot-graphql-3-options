package com.example.unstableservice.controllers;

import com.example.unstableservice.exception.UnexpectedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class UnstableController {

    public static final int BOUND = 5;

    @GetMapping("/unstable")
    public CompletableFuture<ResponseEntity<Product>> unstable() {
        log.info("unstable endpoint called");
        if(new Random().nextInt(BOUND)!=1){
            log.error("unstable endpoint called returning Exception");
            throw new UnexpectedException();
        }
        ResponseEntity<Product> tv = new ResponseEntity<>(new Product("TV", 350.00), HttpStatus.OK);
        log.info("unstable endpoint returning product: {}", tv);
        return CompletableFuture.completedFuture(tv);
    }

    @GetMapping("/unstableSync")
    public ResponseEntity<Product> unstableSync() {
        log.info("unstable endpoint called");
        if(new Random().nextInt(BOUND)!=1){
            log.error("unstable endpoint called returning Exception");
            throw new UnexpectedException();
        }
        ResponseEntity<Product> tv = new ResponseEntity<>(new Product("TV", 350.00), HttpStatus.OK);
        log.info("unstable endpoint returning product: {}", tv);
        return tv;
    }

}
