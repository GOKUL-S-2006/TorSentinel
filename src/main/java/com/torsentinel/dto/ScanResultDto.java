package com.torsentinel.dto;

import com.torsentinel.model.ScanResult;

import java.time.Instant;

public record ScanResultDto(
        Long id,
        String url,
        String status,
        Integer httpStatus,
        Long responseTimeMs,
        String errorMessage,
        Instant checkedAt) {

    public static ScanResultDto from(ScanResult r) {
        return new ScanResultDto(r.getId(), r.getUrl(), r.getStatus().name(),
                r.getHttpStatus(), r.getResponseTimeMs(), r.getErrorMessage(), r.getCheckedAt());
    }
}
