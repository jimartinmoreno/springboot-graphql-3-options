package com.example.api.exception;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import graphql.com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.HttpStatus.resolve;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final List<Integer> retryableStatusCodes = ImmutableList.of(
            REQUEST_TIMEOUT.value(),
            TOO_MANY_REQUESTS.value(),
            INTERNAL_SERVER_ERROR.value(),
            BAD_GATEWAY.value(),
            SERVICE_UNAVAILABLE.value(),
            GATEWAY_TIMEOUT.value()
    );

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (retryableStatusCodes.contains(response.status())) {
                return new RetryableException(response.status(),
                        String.format("%s is a retryable status code, please retry", resolve(response.status())),
                        response.request().httpMethod(), null, response.request());
            }
            byte[] content = toByteArray(response.body().asInputStream());
            String responseBody = new String(content, UTF_8);
            String message = String.format("status %s reading %s : %s", response.status(), methodKey, responseBody);
            return new CustomFeignException(response.status(), message, content);
        } catch (IOException e) {
            String message = String.format("status %s reading %s", response.status(), methodKey);
            return new CustomFeignException(response.status(), message);
        }
    }

    public static class CustomFeignException extends FeignException {
        CustomFeignException(int status, String message, byte[] content) {
            super(status, message, content, new HashMap<>());
        }

        CustomFeignException(int status, String message) {
            super(status, message);
        }
    }

}
