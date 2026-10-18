# TorSentinel
Onion service availability monitoring (Java 21, Spring Boot, Spring Data JPA, PostgreSQL).

## Run
1. `CREATE DATABASE onion_monitor;` (table is auto-created by JPA; optional manual SQL in `docs/schema.sql`)
2. `export DB_PASSWORD=your_password`
3. Start Tor (SOCKS on 127.0.0.1:9050)
4. Edit `data/onion-urls.txt` (authorized services only)
5. `mvn spring-boot:run`
6. `curl -X POST http://localhost:8080/api/scan` and `curl http://localhost:8080/api/results`

Extra endpoint: `GET /api/results/history?url=<onion-url>`
A failed check means unreachable during that attempt, not necessarily permanently offline.
