CREATE TABLE IF NOT EXISTS scan_results (
    id BIGSERIAL PRIMARY KEY,
    url TEXT NOT NULL,
    checked_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    status VARCHAR(30) NOT NULL,
    http_status INTEGER,
    response_time_ms BIGINT NOT NULL,
    error_message TEXT
    );

CREATE INDEX IF NOT EXISTS idx_scan_results_url_checked
ON scan_results (url, checked_at DESC);