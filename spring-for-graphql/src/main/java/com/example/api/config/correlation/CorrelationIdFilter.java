package com.example.api.config.correlation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.example.api.config.graphQL.instrumentation.LoggingInstrumentation.CORRELATION_ID;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class CorrelationIdFilter implements Filter {


    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        String correlationId= extract(request)
                .orElse(UUID.randomUUID().toString());

        /**
         * Podemos incluir el correlationId en el MDC aqui o en el HeaderInterceptor
         */
        log.info("doFilter - CorrelationId: {}", correlationId);
        MDC.put(CORRELATION_ID, correlationId);
        log.info("doFilter - ContextMap: {}", MDC.getCopyOfContextMap());
        chain.doFilter(request, response);
    }

    private Optional<String> extract(ServletRequest request) {
        String correlationId = null;
        if (request instanceof HttpServletRequest httpServletRequest) {
            String correlationIdFromHeader = httpServletRequest.getHeader(CORRELATION_ID);

            if (correlationIdFromHeader != null && !correlationIdFromHeader.trim().isEmpty()) {
                correlationId = correlationIdFromHeader;
            }
        }
        return Optional.ofNullable(correlationId);
    }
}
