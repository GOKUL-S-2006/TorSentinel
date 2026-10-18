package com.torsentinel.dto;

import java.util.List;

public record ScanResponse(int scanned, List<ScanResultDto> results) {
}
