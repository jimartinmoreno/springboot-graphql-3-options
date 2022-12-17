package com.example.api.graphql.controllers.user;

import com.example.api.model.User;
import com.example.api.service.UserService;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserQueryController {

    private final UserService userService;

    private final AsyncTaskExecutor myExecutor;

    @QueryMapping
    public CompletableFuture<List<User>> getUsers() {
        return CompletableFuture.supplyAsync(userService::getAllUsers, myExecutor);
    }
//    @QueryMapping
//    public List<User> getUsers() {
//        return userService.getAllUsers();
//    }

    @QueryMapping
    public CompletableFuture<Optional<User>> getUserById(@Argument Long id) {
        log.info("getUserById for user: {}", id);
        return userService.getUserById(id);
    }

    @QueryMapping
    //public List<User> getUsersByIds(@Argument List<Long> userIds, DataFetchingEnvironment env) {
    public Flux<User> getUsersByIds(@Argument List<Long> ids) {
        log.info("getUsersByIds >>>>>> userIds: {}", ids);
        // Selection fields set requested
        //printEnvironment("getUsersByIds >>>>>> Selection set:", env, "getUsersByIds >>>>> Variables:", "getUsersByIds >>>>> Arguments:");
        return Flux.fromIterable(userService.getAllUsers().stream()
                .filter(user -> ids.contains(user.getId()))
                    .toList());
    }

    private void printEnvironment(String msg, DataFetchingEnvironment env, String msg1, String msg2) {
        log.info(msg);
        env.getSelectionSet().getFields().stream().map(SelectedField::getName).distinct().forEach(log::info);
        // Request variables
        log.info(msg1);
        env.getVariables().entrySet().stream().map(Object::toString).forEach(log::info);
        // Request arguments, filter arguments
        log.info(msg2);
        env.getArguments().entrySet().stream().map(Object::toString).forEach(log::info);
    }
}
