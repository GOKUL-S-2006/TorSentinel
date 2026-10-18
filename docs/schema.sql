-- Optional: Hibernate creates this automatically with ddl-auto=update.
CREATE TABLE IF NOT EXISTS scan_results (
    id               BIGSERIAL PRIMARY KEY,
    url              VARCHAR(2048) NOT NULL,
    checked_at       TIMESTAMPTZ   NOT NULL,
    status           VARCHAR(32)   NOT NULL,
    http_status      INTEGER,
    response_time_ms BIGINT,
    error_message    TEXT
);
CREATE INDEX IF NOT EXISTS idx_scan_results_url ON scan_results (url);
CREATE INDEX IF NOT EXISTS idx_scan_results_checked_at ON scan_results (checked_at);
