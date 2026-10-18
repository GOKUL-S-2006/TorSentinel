package com.torsentinel.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "scan_results", indexes = {
        @Index(name = "idx_scan_results_url", columnList = "url"),
        @Index(name = "idx_scan_results_checked_at", columnList = "checked_at")
})
public class ScanResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(name = "checked_at", nullable = false)
    private Instant checkedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ScanStatus status;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Column(name = "response_time_ms")
    private Long responseTimeMs;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    public ScanResult() {
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Instant getCheckedAt() { return checkedAt; }
    public void setCheckedAt(Instant checkedAt) { this.checkedAt = checkedAt; }
    public ScanStatus getStatus() { return status; }
    public void setStatus(ScanStatus status) { this.status = status; }
    public Integer getHttpStatus() { return httpStatus; }
    public void setHttpStatus(Integer httpStatus) { this.httpStatus = httpStatus; }
    public Long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
