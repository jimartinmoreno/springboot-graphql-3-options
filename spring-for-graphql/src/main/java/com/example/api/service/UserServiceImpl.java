package com.example.api.service;

import com.example.api.client.UserFeignClient;
import com.example.api.exception.GraphQLException;
import com.example.api.model.User;
import com.example.api.model.UserInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserFeignClient userFeignClient;

    @Override
    public List<User> getAllUsers() {
        return userFeignClient.getAllUsers();
    }

    @Override
    public CompletableFuture<Optional<User>> getUserById(Long id) {
        CompletableFuture<Optional<User>> user = userFeignClient.getUserById(id);
        log.info("User: {}", user);
        return user;
    }

    @Override
    public User createUser(UserInput userInput) {
        return userFeignClient.createUser(userInput);
    }

    @Override
    public User updateUser(Long id, UserInput userInput) {
        return userFeignClient.updateUser(id, userInput);
    }

    @Override
    public Boolean deleteUser(Long id) {
        userFeignClient.deleteUser(id);
        return true;
    }
}
