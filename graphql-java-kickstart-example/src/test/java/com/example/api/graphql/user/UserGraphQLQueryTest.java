package com.example.api.graphql.user;

import com.example.api.config.async.ExecutorFactory;
import com.example.api.graphql.resovers.user.UserQueryResolver;
import com.example.api.helper.GraphQLResponseWrapper;
import com.example.api.helper.GraphqlTestConfig;
import com.example.api.model.User;
import com.example.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTest;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(SpringExtension.class)
@GraphQLTest
@ActiveProfiles("test")
//@Import({GraphqlTestConfig.class})
class UserGraphQLQueryTest {

    private static final String GRAPHQL_QUERY_REQUEST_PATH = "graphql/user/request/%s.graphqls";
    private static final String GRAPHQL_QUERY_RESPONSE_PATH = "graphql/user/response/%s.json";

//    @MockBean
//    UserService userService;
//
//    @MockBean
//    Executor myExecutor;

//    @Mock
//    private DataFetchingEnvironment environment;

//    @SpyBean
//    private UserQueryResolver resolver;

    @Autowired
    GraphQLTestTemplate graphQLTestTemplate;  // Helper class to test GraphQL queries and mutations.

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void shouldFindUser() throws IOException {

        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("userId", 1L);

        String testName = "userById2";
        User user = User.builder().id(1L).name("LEANNE GRAHAM").build();
//        when(userService.getUserById(any())).thenReturn(user);

        GraphQLResponse graphQLResponse = graphQLTestTemplate
                .withBasicAuth("admin", "admin")
                .postForResource(String.format(GRAPHQL_QUERY_REQUEST_PATH, testName));

        User userResponse = new GraphQLResponseWrapper(objectMapper, graphQLResponse)
                .get("data.userById", User.class);

        String response = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, testName));

//        verify(resolver).getUserById(any(), any());
        assertEquals(HttpStatus.OK, graphQLResponse.getStatusCode());
        assertThat(response).isEqualTo(graphQLResponse.getRawResponse().getBody());
        assertEquals(response, graphQLResponse.getRawResponse().getBody());
        assertThat(userResponse.getName()).isEqualTo("LEANNE GRAHAM");
        assertThat(userResponse.getId()).isEqualTo(1L);
    }

    private String read(String location) throws IOException {
        return IOUtils.toString(new ClassPathResource(location).getInputStream(), StandardCharsets.UTF_8);
    }
}
