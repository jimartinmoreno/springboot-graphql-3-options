package com.example.api.exception;

import graphql.ExceptionWhileDataFetching;
import graphql.PublicApi;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import graphql.util.LogKit;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
@Slf4j
public class CustomDataFetcherExceptionHandler implements DataFetcherExceptionHandler {

    static final CustomDataFetcherExceptionHandler defaultImpl = new CustomDataFetcherExceptionHandler();

    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = unwrap(handlerParameters.getException());
        SourceLocation sourceLocation = handlerParameters.getSourceLocation();
        ResultPath path = handlerParameters.getPath();

        ExceptionWhileDataFetching error = new ExceptionWhileDataFetching(path, exception, sourceLocation);
        logException(error, exception);

        return DataFetcherExceptionHandlerResult.newResult().error(error).build();
    }

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        return CompletableFuture.completedFuture(onException(handlerParameters));
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
     *
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
