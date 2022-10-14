package com.example.api.graphql.controllers.user;

import com.example.api.model.User;
import com.example.api.model.UserInput;
import com.example.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserMutationController {

    private final UserService userService;

    @MutationMapping
    public User createUser(@Argument @Valid UserInput input) {
        log.info("Creating user for {}", input);
        return userService.createUser(input);
    }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument @Valid UserInput input) {
        log.info("Updating user for {}", input);
        return userService.updateUser(id, input);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        log.info("Deleting user for {}", id);
        return userService.deleteUser(id);
    }
}
