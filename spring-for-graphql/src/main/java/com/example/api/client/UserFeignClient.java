package com.example.api.client;

import com.example.api.model.User;
import com.example.api.model.UserInput;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

//@FeignClient(name = "userFeignClient", url = "https://jsonplaceholder.typicode.com")
public interface UserFeignClient {
    @GetMapping("/users")
    List<User> getAllUsers();

    @GetMapping("/users/{userId}")
    CompletableFuture<Optional<User>> getUserById(@PathVariable Long userId);

    @PostMapping("/users")
    User createUser(UserInput userInput);

    @PutMapping("/users/{userId}")
    User updateUser(@PathVariable Long userId, UserInput userInput);

    @DeleteMapping("/users/{userId}")
    void deleteUser(@PathVariable Long userId);
}
