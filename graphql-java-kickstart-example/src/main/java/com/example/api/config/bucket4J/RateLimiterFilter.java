package com.example.api.config.bucket4J;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class RateLimiterFilter implements Filter {

    private final RateLimiterManager rateLimiterManager;

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String userId = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Principal::getName)
                .orElse(null);
        //String userId = request.getHeader("Origin-System-Id");
        boolean bellowTheRateLimit = rateLimiterManager.isBellowTheRateLimit(userId);
        log.info("doFilter - userId: {}", userId);
        log.info("doFilter - bellowTheRateLimit: {}", bellowTheRateLimit);
        //if (bellowTheRateLimit) {
        chain.doFilter(request, response);
        //} else {
        //    response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have exhausted your API Request Quota");
        //}
    }
}
