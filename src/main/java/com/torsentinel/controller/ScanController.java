package com.torsentinel.controller;

import com.torsentinel.dto.ScanResponse;
import com.torsentinel.dto.ScanResultDto;
import com.torsentinel.service.ScanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ScanController {

    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping("/scan")
    public ScanResponse scan() throws IOException {
        return scanService.runScan();
    }

    @GetMapping("/results")
    public List<ScanResultDto> results() {
        return scanService.recentResults();
    }

    /** Extra: history for a single URL, e.g. /api/results/history?url=http://abc.onion */
    @GetMapping("/results/history")
    public List<ScanResultDto> history(@RequestParam String url) {
        return scanService.historyFor(url);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleIo(IOException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
}
