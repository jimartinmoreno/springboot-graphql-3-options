package com.example.api.config.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;


import static com.example.api.config.security.SecurityConfig.USER_ID_PRE_AUTH_HEADER;
import static com.example.api.config.graphQL.instrumentation.LoggingInstrumentation.CORRELATION_ID;

/**
 * Handle pre-authenticated authentication requests, where it is assumed that the principal has already been authenticated
 * by an external system.
 * The purpose is then only to extract the necessary information on the principal from the incoming request, rather than
 * to authenticate them.
 */
@Slf4j
public class RequestHeadersPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        log.info("getPreAuthenticatedPrincipal - headers: {}", request.getHeaderNames());
        log.info("getPreAuthenticatedPrincipal - CorrelationId from header: {}", request.getHeader(CORRELATION_ID));
        return request.getHeader(USER_ID_PRE_AUTH_HEADER);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        log.info("getPreAuthenticatedPrincipal - headers: {}", request.getHeaderNames());
        log.info("getPreAuthenticatedPrincipal - CorrelationId from header: {}", request.getHeader(CORRELATION_ID));
        return StringUtils.EMPTY;
    }
}
