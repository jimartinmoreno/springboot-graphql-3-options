package com.example.api.config.bucket4J;

import io.github.bucket4j.Bandwidth;

import java.time.Duration;

public final class BandwidthBuilder {

    public static Bandwidth buildBandwidth(String userId) {
        if ("manager".equals(userId)) {
            return Bandwidth.simple(10, Duration.ofMinutes(1));
        } else if ("user".equals(userId)) {
            return Bandwidth.simple(3, Duration.ofMinutes(1));
        } else {
            return Bandwidth.simple(10000, Duration.ofMinutes(1));
        }
    }
}
