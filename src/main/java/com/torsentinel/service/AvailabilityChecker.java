package com.torsentinel.service;

import com.torsentinel.config.MonitorProperties;
import com.torsentinel.model.ScanResult;
import com.torsentinel.model.ScanStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.time.Instant;

/** Performs one availability check through the local Tor SOCKS proxy. */
@Component
public class AvailabilityChecker {

    private final MonitorProperties props;
    private final Proxy torProxy;

    public AvailabilityChecker(MonitorProperties props) {
        this.props = props;
        this.torProxy = new Proxy(Proxy.Type.SOCKS,
                new InetSocketAddress(props.socksHost(), props.socksPort()));
    }

    public ScanResult check(String rawUrl) {
        ScanResult result = new ScanResult();
        result.setUrl(rawUrl);
        result.setCheckedAt(Instant.now());

        URI uri;
        try {
            uri = validate(rawUrl);
        } catch (IllegalArgumentException e) {
            result.setStatus(ScanStatus.INVALID_URL);
            result.setErrorMessage(e.getMessage());
            return result;
        }

        long start = System.nanoTime();
        HttpURLConnection conn = null;
        try {
            // Passing the proxy lets Tor resolve the .onion hostname (no local DNS lookup).
            conn = (HttpURLConnection) uri.toURL().openConnection(torProxy);
            conn.setConnectTimeout(props.timeoutMs());
            conn.setReadTimeout(props.timeoutMs());
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-Agent", "TorSentinel/1.0 (availability monitor)");

            int code = conn.getResponseCode();
            result.setHttpStatus(code);
            result.setResponseTimeMs(elapsedMs(start));

            if (code >= 200 && code < 400) {
                result.setStatus(ScanStatus.REACHABLE);
            } else {
                result.setStatus(ScanStatus.HTTP_ERROR);
                result.setErrorMessage("HTTP " + code);
            }
        } catch (SocketTimeoutException e) {
            result.setStatus(ScanStatus.TIMEOUT);
            result.setResponseTimeMs(elapsedMs(start));
            result.setErrorMessage("Timed out after " + props.timeoutMs() + " ms");
        } catch (IOException e) {
            result.setStatus(ScanStatus.CONNECTION_FAILED);
            result.setResponseTimeMs(elapsedMs(start));
            result.setErrorMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    private URI validate(String rawUrl) {
        URI uri;
        try {
            uri = new URI(rawUrl.trim());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Malformed URL: " + e.getReason());
        }
        String scheme = uri.getScheme();
        if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
            throw new IllegalArgumentException("Scheme must be http or https");
        }
        String host = uri.getHost();
        if (host == null || !host.toLowerCase().endsWith(".onion")) {
            throw new IllegalArgumentException("Host must be a .onion address");
        }
        return uri;
    }

    private long elapsedMs(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000;
    }
}
