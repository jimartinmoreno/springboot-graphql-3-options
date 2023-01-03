package com.example.unstableservice.controllers;

import com.example.unstableservice.exception.UnexpectedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class UnstableController {

    public static final int BOUND = 5;

    @GetMapping("/unstable")
    public CompletableFuture<ResponseEntity<Product>> unstable() {
        log.info("-------------------");
        log.info("unstable endpoint called");
        if (new Random().nextInt(BOUND) == 2) {
            log.error("unstable endpoint called returning UnexpectedException");
            throw new UnexpectedException();
        } else if (new Random().nextInt(BOUND) == 3) {
            log.error("unstable endpoint called returning REQUEST_TIMEOUT");
            return CompletableFuture.completedFuture(new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT));
        } else if (new Random().nextInt(BOUND) == 4) {
            log.error("unstable endpoint called returning TOO_MANY_REQUESTS");
            return CompletableFuture.completedFuture(new ResponseEntity<>(null, HttpStatus.TOO_MANY_REQUESTS));
        } else if (new Random().nextInt(BOUND) == 5) {
            log.error("unstable endpoint called returning SERVICE_UNAVAILABLE");
            return CompletableFuture.completedFuture(new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE));
        }
//        log.error("unstable endpoint called returning SERVICE_UNAVAILABLE");
//        return CompletableFuture.completedFuture(new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE));
        ResponseEntity<Product> tv = new ResponseEntity<>(new Product("TV", 350.00), HttpStatus.OK);
        log.info("unstable endpoint returning product: {}", tv);
        return CompletableFuture.completedFuture(tv);
    }

    @GetMapping("/unstableSync")
    public ResponseEntity<Product> unstableSync() {
        log.info("-------------------");
        log.info("unstable SYNC endpoint called");
        if (new Random().nextInt(BOUND) == 2) {
            log.error("unstable endpoint called returning UnexpectedException");
//            throw new UnexpectedException();
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (new Random().nextInt(BOUND) == 3) {
            log.error("unstable endpoint called returning REQUEST_TIMEOUT");
            return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
        } else if (new Random().nextInt(BOUND) == 4) {
            log.error("unstable endpoint called returning TOO_MANY_REQUESTS");
            return new ResponseEntity<>(null, HttpStatus.TOO_MANY_REQUESTS);
        } else if (new Random().nextInt(BOUND) == 5) {
            log.error("unstable endpoint called returning SERVICE_UNAVAILABLE");
            return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
        }
//        log.error("unstable endpoint called returning SERVICE_UNAVAILABLE");
//        return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
        ResponseEntity<Product> tv = new ResponseEntity<>(new Product("TV", 350.00), HttpStatus.OK);
        log.info("unstable endpoint returning product: {}", tv);
        return tv;
    }

}
