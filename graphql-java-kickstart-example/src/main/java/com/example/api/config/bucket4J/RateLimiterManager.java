package com.example.api.config.bucket4J;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimiterManager {

    private final BucketManager bucketManager;

    public boolean isBellowTheRateLimit(String userId) {
        Bucket tokenBucket = bucketManager.resolveBucket(userId);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        log.info("isBellowTheRateLimit - probe: {}", probe);
        if (!probe.isConsumed()) {
            log.info("isBellowTheRateLimit - Too many request for user: {}", userId);
            return false;
        }
        log.info("isBellowTheRateLimit - Consumed for user: {}", userId);
        return true;
    }
}
