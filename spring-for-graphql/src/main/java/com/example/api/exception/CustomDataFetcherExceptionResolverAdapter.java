package com.example.api.exception;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletionException;

@Slf4j
@Component
public class CustomDataFetcherExceptionResolverAdapter extends DataFetcherExceptionResolverAdapter {

    protected CustomDataFetcherExceptionResolverAdapter() {
        super();
    }

    @Override
    protected List<GraphQLError> resolveToMultipleErrors(Throwable ex, DataFetchingEnvironment env) {
        log.info("resolveToMultipleErrors - Exception: {}, env: {}", ex, env);
        return super.resolveToMultipleErrors(ex, env);
    }

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        log.info("resolveToSingleError - Exception: {}, env: {}", ex, env);
        Throwable exception = unwrap(ex);

        SourceLocation sourceLocation = env.getField().getSourceLocation();
        ResultPath path =  env.getExecutionStepInfo().getPath();

        //        return GraphqlErrorBuilder.newError()
        //                .errorType(ErrorType.NOT_FOUND)
        //                .message(ex.getMessage())
        //                .path(env.getExecutionStepInfo().getPath())
        //                .location(env.getField().getSourceLocation())
        //                .build();

        ExceptionWhileDataFetching error = new ExceptionWhileDataFetching(path, exception, sourceLocation);
        logException(error, exception);
        return error;
    }

    /**
     * Called to log the exception - a subclass could choose to something different in logging terms
     *
     * @param error     the graphql error
     * @param exception the exception that happened
     */
    protected void logException(ExceptionWhileDataFetching error, Throwable exception) {
        log.warn(error.getMessage(), exception);
    }

    /**
     * Called to unwrap an exception to a more suitable cause if required.
     *
     * @param exception the exception to unwrap
     * @return the suitable exception
     */
    protected Throwable unwrap(Throwable exception) {
        if (exception.getCause() != null) {
            if (exception instanceof CompletionException) {
                return exception.getCause();
            }
        }
        return exception;
    }
}
