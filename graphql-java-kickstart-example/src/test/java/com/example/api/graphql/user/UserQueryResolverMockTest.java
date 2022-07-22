package com.example.api.graphql.user;

import com.example.api.helper.GraphQLResponseWrapper;
import com.example.api.helper.error.Error;
import com.example.api.model.User;
import com.example.api.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
//@GraphQLTest
class UserQueryResolverMockTest {

    private static final String GRAPHQL_QUERY_REQUEST_PATH = "graphql/user/request/%s.graphqls";
    private static final String GRAPHQL_QUERY_RESPONSE_PATH = "graphql/user/response/%s.json";

    @Autowired
    GraphQLTestTemplate graphQLTestTemplate;  // Helper class to test GraphQL queries and mutations.
    @Autowired
    ObjectMapper objectMapper;

    //@MockBean
    //UserService userService;

    @Test
    void user_are_returned() throws IOException {

        //when(userService.getUserById(any())).thenReturn(User.builder().id(2L).name("perico de los palotes").build());
        //doReturn(user2).when(userService).getUserById(1L);
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("userId", 1L);

        String testName = "userById";

        GraphQLResponse graphQLResponse = graphQLTestTemplate
                //.withBasicAuth("admin", "admin")
                .perform(String.format(GRAPHQL_QUERY_REQUEST_PATH, testName), variables);

        User user = new GraphQLResponseWrapper(objectMapper, graphQLResponse)
                .get("data.userById", User.class);

        String response = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, testName));

        assertEquals(HttpStatus.OK, graphQLResponse.getStatusCode());
        assertThat(response).isEqualTo(graphQLResponse.getRawResponse().getBody());
        assertEquals(response, graphQLResponse.getRawResponse().getBody());
        assertThat(user.getName()).isEqualTo("LEANNE GRAHAM");
        assertThat(user.getId()).isEqualTo(1L);
    }


    @Test
    void users_are_returned() throws IOException {

        String testName = "usersByIds";
        ObjectNode variables = new ObjectMapper().createObjectNode();

        GraphQLResponse graphQLResponse = graphQLTestTemplate
                .withBasicAuth("admin", "admin")
                .perform(String.format(GRAPHQL_QUERY_REQUEST_PATH, testName), variables);

        List<User> users = new GraphQLResponseWrapper(objectMapper, graphQLResponse)
                .getList("data.usersByIds", List.class);

        String response = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, testName));

        assertEquals(HttpStatus.OK, graphQLResponse.getStatusCode());
        assertThat(response).isEqualTo(graphQLResponse.getRawResponse().getBody());
        assertEquals(response, graphQLResponse.getRawResponse().getBody());
        assertThat(users.size()).isEqualTo(1);
    }


    @Test
    void users_not_returned_no_access_permissions() throws IOException {

        String testName = "usersByIds";
        ObjectNode variables = new ObjectMapper().createObjectNode();

        GraphQLResponse graphQLResponse = graphQLTestTemplate
                .withBasicAuth("user", "user")
                .perform(String.format(GRAPHQL_QUERY_REQUEST_PATH, testName), variables);

        List<Error> errors = new GraphQLResponseWrapper(objectMapper, graphQLResponse)
                .getList("errors", new TypeReference<>() {
                });

        System.out.println("errors = " + errors);
        System.out.println("error = " + errors.get(0).getMessage());
        assertEquals(HttpStatus.OK, graphQLResponse.getStatusCode());
        assertThat(errors.get(0).getMessage()).contains("No access permission");

        List<Map> errorsMaps = new GraphQLResponseWrapper(objectMapper, graphQLResponse)
                .getList("errors", List.class);

        System.out.println("errorsMaps = " + errorsMaps);
        System.out.println("error = " + errorsMaps.get(0).get("message"));
        assertThat(errorsMaps.get(0).get("message").toString()).contains("No access permission");
    }


    private String read(String location) throws IOException {
        return IOUtils.toString(new ClassPathResource(location).getInputStream(), StandardCharsets.UTF_8);
    }
}
