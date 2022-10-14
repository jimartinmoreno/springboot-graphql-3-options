package com.example.api.exception;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * Handler to handle the returned list of errors. the default implementation is the DefaultGraphQLErrorHandler
 */
@Component
@Slf4j
public class CustomGraphQLErrorHandler implements GraphQLErrorHandler {

    @ExceptionHandler
    public ThrowableGraphQLError handle(Exception e) {
        log.warn("handle Exception: {}", e.getMessage());
        return new ThrowableGraphQLError(e);
    }

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        log.info("processErrors Errors:  {}", errors);

        //        if (!errors.isEmpty()) {
        //            errors.stream().forEach(graphQLError -> {
        //                log.info("Error type:  {}", graphQLError.getErrorType());
        //                log.info("Error message:  {}", graphQLError.getMessage());
        //                log.info("Error path:  {}", graphQLError.getPath());
        //                log.info("Error locations:  {}", graphQLError.getLocations());
        //            });
        //        }
        return errors;
        //return errors.stream().map(this::getNested).toList();
    }

    private GraphQLError getNested(GraphQLError error) {
        if (error instanceof RuntimeException) {
            ExceptionWhileDataFetching exceptionError = (ExceptionWhileDataFetching) error;
            if (exceptionError.getException() instanceof GraphQLError) {
                return (GraphQLError) exceptionError.getException();
            }
        }
        return error;
    }
}
