package com.example.api.helper;

import com.example.api.helper.error.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GraphqlTestHelper {
    private final GraphQLTestTemplate graphQLTestTemplate;
    private final ObjectMapper objectMapper;

    public <T> GraphQLResponseWrapper performRequest(String queryFilePath, Input<T> input) throws IOException {
        GraphQLResponse response = graphQLTestTemplate.perform(queryFilePath, getAsQueryVariables(input));
        return new GraphQLResponseWrapper(objectMapper, response);
    }

    public void addHeader(String name, String value) {
        graphQLTestTemplate.withAdditionalHeader(name, value);
    }

    private <T> ObjectNode getAsQueryVariables(Input<T> input) throws IOException {
        String requestJson = objectMapper.writeValueAsString(input);
        return objectMapper.readValue(requestJson, ObjectNode.class);
    }

    @RequiredArgsConstructor
    public static class GraphQLResponseWrapper {
        private final ObjectMapper objectMapper;
        private final GraphQLResponse response;

        public List<?> getErrors() {
            return response.get("errors", List.class);
        }

        public <T> T get(String path, Class<T> clazz) {
            log.info("get path: {}, clazz: {}", path, clazz);
            try {
                String errors = response.get("errors", Object.class).toString();
                log.info("Errors: {}", errors);
            } catch (PathNotFoundException e) {

            }
            log.info("Body: {}", response.getRawResponse().getBody());
            return objectMapper.convertValue(response.get(path, Map.class), clazz);
        }
    }
}
