package com.torsentinel.service;

import com.torsentinel.dto.ScanResponse;
import com.torsentinel.dto.ScanResultDto;
import com.torsentinel.model.ScanResult;
import com.torsentinel.repository.ScanResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScanService {

    private static final Logger log = LoggerFactory.getLogger(ScanService.class);

    private final UrlLoader urlLoader;
    private final AvailabilityChecker checker;
    private final ScanResultRepository repository;

    public ScanService(UrlLoader urlLoader, AvailabilityChecker checker, ScanResultRepository repository) {
        this.urlLoader = urlLoader;
        this.checker = checker;
        this.repository = repository;
    }

    /** Scans every configured URL, stores each result, and returns them. */
    public ScanResponse runScan() throws IOException {
        List<String> urls = urlLoader.load();
        List<ScanResult> results = new ArrayList<>();
        for (String url : urls) {
            ScanResult r = checker.check(url);
            log.info("Checked {} -> {} ({} ms)", url, r.getStatus(), r.getResponseTimeMs());
            results.add(repository.save(r));
        }
        return new ScanResponse(results.size(), results.stream().map(ScanResultDto::from).toList());
    }

    public List<ScanResultDto> recentResults() {
        return repository.findTop50ByOrderByCheckedAtDesc().stream().map(ScanResultDto::from).toList();
    }

    public List<ScanResultDto> historyFor(String url) {
        return repository.findTop100ByUrlOrderByCheckedAtDesc(url).stream().map(ScanResultDto::from).toList();
    }
}
