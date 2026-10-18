package com.torsentinel;

import com.torsentinel.config.MonitorProperties;
import com.torsentinel.model.ScanStatus;
import com.torsentinel.service.AvailabilityChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AvailabilityCheckerTest {

    private final AvailabilityChecker checker =
            new AvailabilityChecker(new MonitorProperties("x", "127.0.0.1", 9050, 1000));

    @Test
    void rejectsNonOnionHost() {
        assertEquals(ScanStatus.INVALID_URL, checker.check("http://example.com").getStatus());
    }

    @Test
    void rejectsBadScheme() {
        assertEquals(ScanStatus.INVALID_URL, checker.check("ftp://abc.onion").getStatus());
    }
}
