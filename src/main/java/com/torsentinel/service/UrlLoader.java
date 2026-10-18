package com.torsentinel.service;

import com.torsentinel.config.MonitorProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;

/** Reads the URL inventory file: one URL per line, '#' comments and blank lines ignored. */
@Component
public class UrlLoader {

    private final MonitorProperties props;

    public UrlLoader(MonitorProperties props) {
        this.props = props;
    }

    public List<String> load() throws IOException {
        Path file = Path.of(props.urlFile());
        if (!Files.exists(file)) {
            throw new IOException("URL file not found: " + file.toAbsolutePath());
        }
        LinkedHashSet<String> urls = new LinkedHashSet<>(); // de-duplicates, keeps order
        for (String line : Files.readAllLines(file)) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                urls.add(trimmed);
            }
        }
        return List.copyOf(urls);
    }
}
