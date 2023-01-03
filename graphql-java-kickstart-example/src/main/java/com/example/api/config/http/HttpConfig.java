package com.example.api.config.http;

import com.example.api.client.ProductSpringHttpInterfacesClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Configuration
public class HttpConfig {
    @Bean
    public ProductSpringHttpInterfacesClient productSpringHttpInterfacesClient() {
        HttpClient httpClient = getHttpClient();

        //ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .clientConnector(connector)
                .defaultHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
        return factory.createClient(ProductSpringHttpInterfacesClient.class);
    }

    @NotNull
    private static HttpClient getHttpClient() {
        return HttpClient.create()
                .responseTimeout(Duration.ofMillis(100));
//                .tcpConfiguration(client ->
//                        client
//                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100)
//                                .doOnConnected(conn -> conn
//                                        .addHandlerLast(new ReadTimeoutHandler(50))
//                                        .addHandlerLast(new WriteTimeoutHandler(50))
//                                )
//                );
    }
}
