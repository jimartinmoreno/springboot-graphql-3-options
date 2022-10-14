package com.example.api.graphql.controllers.user;

import com.example.api.model.User;
import com.example.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.execution.DefaultExecutionGraphQlService;
import org.springframework.graphql.test.tester.ExecutionGraphQlServiceTester;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

/**
 * GraphQL requests via {@link GraphQlTester}.
 */
@SpringBootTest
@AutoConfigureHttpGraphQlTester
class UserQueryControllerTest {

    @Autowired
    private WebGraphQlTester graphQlTester;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {
        given(this.userService.getAllUsers()).willReturn(List.of(
                User.builder()
                        .id(1L)
                        .name("Bob")
                        .build()
        ));
    }

    @Test
    void anonymousThenUnauthorized() {
        assertThatThrownBy(() ->
                this.graphQlTester.mutate()
                        .build()
                        .documentName("getUsers")
                        .executeAndVerify())
                .hasMessage("Status expected:<200 OK> but was:<401 UNAUTHORIZED>");
    }

    @Test
    void userRoleThenForbidden() {
        WebGraphQlTester tester = this.graphQlTester.mutate()
                .headers(headers -> headers.setBasicAuth("user", "user"))
                .build();

        tester.documentName("getUsers")
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getMessage()).contains("No access permission");
                });
    }

    @Test
    void invalidCredentials() {
        assertThatThrownBy(() ->
                this.graphQlTester.mutate()
                        .headers(headers -> headers.setBasicAuth("admin", "INVALID"))
                        .build()
                        .documentName("getUsers")
                        .executeAndVerify())
                .hasMessage("Status expected:<200 OK> but was:<401 UNAUTHORIZED>");
    }

    @Test
    void getUsers() {
        WebGraphQlTester tester = this.graphQlTester.mutate()
                .headers(headers -> headers.setBasicAuth("admin", "admin"))
                .build();

        tester.documentName("getUsers")
                //.variable("slug", "spring-framework")
                .execute()
                .path("getUsers[0].id").entity(String.class).isEqualTo("1")
                .path("getUsers[0].name").entity(String.class).isEqualTo("Bob")
                .path("getUsers")
                .entityList(User.class)
                .hasSize(1);
    }

    @Test
    void getUserById() {
    }

    @Test
    void getUsersByIds() {
    }
}