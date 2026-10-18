package com.torsentinel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Binds the monitor.* keys from application.properties. */
@ConfigurationProperties(prefix = "monitor")
public record MonitorProperties(
        @DefaultValue("data/onion-urls.txt") String urlFile,
        @DefaultValue("127.0.0.1") String socksHost,
        @DefaultValue("9050") int socksPort,
        @DefaultValue("10000") int timeoutMs) {
}
