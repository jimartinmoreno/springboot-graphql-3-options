package com.example.api.config.feign;

import feign.AsyncClient;
import feign.AsyncFeign;
import feign.Contract;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.hc5.AsyncApacheHttp5Client;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.TimeUnit;

@Configuration
@Import(FeignClientsConfiguration.class)
@Slf4j
public class Hc5AsyncConfig {

    private CloseableHttpAsyncClient httpAsyncClient;

    @Bean
    public PoolingAsyncClientConnectionManager asyncClientConnectionManager(
            FeignHttpClientProperties httpClientProperties) {
        return PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnTotal(httpClientProperties.getMaxConnections())
                .setMaxConnPerRoute(httpClientProperties.getMaxConnectionsPerRoute())
                .setTlsStrategy(ClientTlsStrategyBuilder.create()
                        .setSslContext(SSLContexts.createSystemDefault())
                        .setTlsVersions(TLS.V_1_3, TLS.V_1_2)
                        .build())
                .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.LAX)
                .setConnPoolPolicy(PoolReusePolicy.LIFO)
                .setValidateAfterInactivity(Timeout.ofMilliseconds(httpClientProperties.getConnectionTimerRepeat()))
                .setConnectionTimeToLive(TimeValue.of(httpClientProperties.getTimeToLive(),
                        httpClientProperties.getTimeToLiveUnit()))
                .build();
    }

    @Bean
    public FeignClientProperties.FeignClientConfiguration defaultClientConfiguration(FeignClientProperties feignClientProperties) {
        FeignClientProperties.FeignClientConfiguration feignClientConfiguration = feignClientProperties.getConfig()
                .get(feignClientProperties.getDefaultConfig());
        feignClientConfiguration.setLoggerLevel(Logger.Level.FULL);
        return feignClientConfiguration;
    }

    @Bean
    public CloseableHttpAsyncClient closableHttpAsyncClient(PoolingAsyncClientConnectionManager asyncClientConnectionManager,
                                                            FeignHttpClientProperties httpClientProperties,
                                                            FeignClientProperties.FeignClientConfiguration defaultClientConfiguration) {
        final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(httpClientProperties.getConnectionTimerRepeat()))
                .build();
        httpAsyncClient = HttpAsyncClients.custom()
                .disableCookieManagement()
                .useSystemProperties() // Need for proxy
                .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_1) // Need for proxy
                .setConnectionManager(asyncClientConnectionManager)
                .evictExpiredConnections().setIOReactorConfig(ioReactorConfig) // evict expired connections
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.of(defaultClientConfiguration.getConnectTimeout(), TimeUnit.MILLISECONDS))
                        .setResponseTimeout(Timeout.of(defaultClientConfiguration.getReadTimeout(), TimeUnit.MILLISECONDS))
                        .setCookieSpec(StandardCookieSpec.STRICT)
                        .build())
                //                .setRoutePlanner(new C2cAwareRoutePlanner(INSTANCE, getDefault())) // skip proxy for C2C endpoints
                .build();
        httpAsyncClient.start();
        return httpAsyncClient;
    }

    @Bean
    public AsyncClient<HttpClientContext> asyncClient(CloseableHttpAsyncClient httpAsyncClient) {
        return new AsyncApacheHttp5Client(httpAsyncClient);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public AsyncFeign.AsyncBuilder<HttpClientContext> defaultAsyncBuilder(AsyncClient<HttpClientContext> httpClient,
                                                                          Decoder decoder,
                                                                          Encoder encoder,
                                                                          Contract contract,
                                                                          ErrorDecoder errorDecoder,
                                                                          GlobalFeignRequestInterceptor globalFeignRequestInterceptor) {
        return AsyncFeign.<HttpClientContext>builder()
                .client(httpClient)
                .decoder(decoder)
                .errorDecoder(errorDecoder)
                .encoder(encoder)
                .dismiss404()
                .contract(contract)
                .requestInterceptor(globalFeignRequestInterceptor);
    }

    @PreDestroy
    public void destroy() {
        if (httpAsyncClient != null) {
            httpAsyncClient.close(CloseMode.GRACEFUL);
            log.info("HttpAsyncClient has been closed");
        }
    }
}
