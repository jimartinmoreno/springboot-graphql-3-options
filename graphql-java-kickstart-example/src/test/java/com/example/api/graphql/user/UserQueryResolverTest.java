package com.example.api.graphql.user;

import com.example.api.KickStartApiApplication;
import com.example.api.helper.GraphQLResponseWrapper;
import com.example.api.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KickStartApiApplication.class)
//@GraphQLTest
@ActiveProfiles("test")
class UserQueryResolverTest {

    private static final String GRAPHQL_QUERY_REQUEST_PATH = "graphql/user/request/%s.graphqls";
    private static final String GRAPHQL_QUERY_RESPONSE_PATH = "graphql/user/response/%s.json";

    @Autowired
    GraphQLTestTemplate graphQLTestTemplate;  // Helper class to test GraphQL queries and mutations.
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void user_are_returned() throws IOException {

        graphQLTestTemplate
                .withAdditionalHeader("Authorization", "Basic " + Base64Utils.encodeToString("admin:admin".getBytes(StandardCharsets.UTF_8)));

        String testName = "user";
        GraphQLResponse graphQLResponse = graphQLTestTemplate
                .postForResource(String.format(GRAPHQL_QUERY_REQUEST_PATH, testName));

        //User user = objectMapper.convertValue(graphQLResponse.get("data.userById", Map.class), User.class);

        User user = new GraphQLResponseWrapper(objectMapper, graphQLResponse)
                .get("data.userById", User.class);

        String response = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, testName));

        assertEquals(HttpStatus.OK, graphQLResponse.getStatusCode());
        assertEquals(response, graphQLResponse.getRawResponse().getBody());
        assertThat(user.getName()).isEqualTo("LEANNE GRAHAM");
        assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    void create_post() throws IOException {
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("userId", 1L);

        String testName = "userById";
        GraphQLResponse graphQLResponse = graphQLTestTemplate
                .withBasicAuth("admin", "admin")
                .perform(String.format(GRAPHQL_QUERY_REQUEST_PATH, testName), variables);

        User user = new GraphQLResponseWrapper(objectMapper, graphQLResponse)
                .get("data.userById", User.class);

        String response = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, testName));

        assertEquals(HttpStatus.OK, graphQLResponse.getStatusCode());
        assertEquals(response, graphQLResponse.getRawResponse().getBody());
        assertThat(user.getName()).isEqualTo("LEANNE GRAHAM");
        assertThat(user.getId()).isEqualTo(1L);
    }

    private String read(String location) throws IOException {
        return IOUtils.toString(new ClassPathResource(location).getInputStream(), StandardCharsets.UTF_8);
    }
}
