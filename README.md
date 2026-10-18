# 🛡️ TorSentinel
### Onion Service Availability Monitoring & Threat Intelligence

TorSentinel is a Java and Spring Boot–based monitoring platform designed to check the availability of configured Tor onion services, measure response times, and maintain historical scan records in PostgreSQL.

The project aims to provide a structured way to monitor onion-service connectivity and analyze availability trends through persistent monitoring data.

> **Purpose:** Defensive security monitoring, availability analysis, and cybersecurity research.

## ✨ Features

- **Bulk URL Monitoring** — Load and check multiple `.onion` URLs from a file.
- **Tor Network Connectivity** — Route HTTP(S) requests through a locally running Tor SOCKS proxy.
- **Availability Checks** — Record whether a service responds successfully or encounters an HTTP or connection error.
- **Response-Time Tracking** — Measure how long each availability check takes.
- **PostgreSQL Integration** — Store scan results and maintain historical records.
- **Error Classification** — Distinguish timeouts, connection failures, invalid URLs, and HTTP errors.
- **REST API** — Trigger scans and retrieve stored results through Spring Boot endpoints.
- **Extensible Architecture** — Designed for future scheduling, analytics, and dashboard integration.

## 🧰 Tech Stack

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Spring Boot | Backend application and REST APIs |
| Spring JPA | Database operations |
| PostgreSQL | Persistent scan history |
| Tor SOCKS Proxy | Connectivity to onion services |
| Maven | Dependency management and build |
| Git & GitHub | Version control |

## 🏗️ Architecture

```text
              URL Inventory
             onion-urls.txt
                    |
                    v
          Spring Boot Application
                    |
             URL Validation
                    |
                    v
          Availability Checker
                    |
                    v
             Tor SOCKS Proxy
                    |
                    v
             Tor Network
                    |
                    v
           Onion Service (.onion)
                    |
                    v
          Scan Result Processing
                    |
                    v
               PostgreSQL
                    |
                    v
           REST API / Dashboard
```

## ⚙️ How It Works

1. Load the configured onion URLs from a text file.
2. Validate each URL before making a request.
3. Route HTTP(S) requests through the local Tor SOCKS proxy.
4. Record the HTTP status code, response time, and any connection errors.
5. Save each scan result in PostgreSQL.
6. Retrieve historical scan records through the REST API.

A failed check indicates that the service could not be reached during that attempt. It does not necessarily mean that the service is permanently offline.

## 🗄️ Database Design

The `scan_results` table stores the history of availability checks.

| Column | Description |
|---|---|
| `id` | Unique scan-result identifier |
| `url` | Monitored onion URL |
| `checked_at` | Time of the check |
| `status` | Result classification |
| `http_status` | HTTP response code, when available |
| `response_time_ms` | Elapsed request time in milliseconds |
| `error_message` | Error details, if applicable |

Each scan creates a historical record, allowing availability and response-time trends to be analyzed over time.

## 🔌 REST API

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/scan` | Trigger an availability scan |
| `GET` | `/api/results` | Retrieve recent scan results |

**Example request**

```http
POST /api/scan
```

**Example response**

```json
{
  "scanned": 2,
  "results": [
    {
      "url": "http://example.onion",
      "status": "REACHABLE",
      "httpStatus": 200,
      "responseTimeMs": 1450,
      "errorMessage": null
    }
  ]
}
```

*The response is illustrative; actual results depend on the services being monitored and the Tor connection.*

## 🚀 Getting Started

### Prerequisites

- Java 21 or a compatible configured JDK
- Maven
- PostgreSQL
- Tor running locally with its SOCKS proxy enabled
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/TorSentinel.git
cd TorSentinel
```

Replace `YOUR_USERNAME` with your GitHub username.

### 2. Create the Database

```sql
CREATE DATABASE onion_monitor;
```

Create the `scan_results` table using the SQL schema provided in the project's database setup instructions.

### 3. Configure the Application

Update `src/main/resources/application.properties`:

```properties
spring.application.name=TorSentinel

server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/onion_monitor
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

monitor.url-file=data/onion-urls.txt
monitor.socks-host=127.0.0.1
monitor.socks-port=9050
monitor.timeout-ms=10000
```

Set the `DB_PASSWORD` environment variable before running the application.

### 4. Add URLs to Monitor

Create `data/onion-urls.txt` and add one authorized URL per line:

```text
# Add authorized test services below
http://your-authorized-test-service.onion
```

Lines beginning with `#` and blank lines are ignored.

### 5. Start Tor

Ensure Tor is running and its SOCKS proxy is listening on the configured host and port. A local port check alone does not guarantee successful connectivity through the Tor network.

### 6. Run the Application

Using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

### 7. Test the API

Trigger a scan using curl:

```bash
curl -X POST http://localhost:8080/api/scan
```

Retrieve recent results:

```bash
curl http://localhost:8080/api/results
```

## 🔒 Responsible Use

TorSentinel is intended for authorized monitoring and defensive cybersecurity research.

- Monitor only services you are permitted to assess.
- Use conservative request timeouts and bounded concurrency.
- Avoid excessive requests that could burden monitored services or the Tor network.
- Do not collect credentials, private communications, or unnecessary page content.
- Treat connection failures as observations, not proof of malicious activity or permanent downtime.
- Protect database records and operational logs from unauthorized access.

## 🗺️ Future Enhancements

- [ ] Scheduled scans using Spring Scheduler
- [ ] Bounded concurrent scanning for larger URL inventories
- [ ] Separate monitored-service inventory and scan-history tables
- [ ] Historical availability and response-time analytics
- [ ] React-based monitoring dashboard
- [ ] CSV and JSON report exports
- [ ] Scan progress tracking and cancellation
- [ ] Authentication and role-based access control
- [ ] Docker-based local development environment
- [ ] Improved classification of obsolete onion addresses and Tor connectivity failures

## 🎯 Learning Objectives

This project provides practical experience with:

- Building REST APIs using Spring Boot
- Integrating Java applications with PostgreSQL
- Working with HTTP connections and SOCKS proxies
- Handling network timeouts and exceptions
- Designing database schemas for historical monitoring
- Structuring backend services for scalability and maintainability

## 🤝 Contributing

Contributions that improve reliability, documentation, testing, and defensive monitoring capabilities are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Make and test your changes.
4. Submit a pull request describing the improvement.

## 📄 License

Choose a license before publishing the project. The MIT License is one option if you want others to reuse, modify, and distribute your code under its terms.

---

**TorSentinel — Observe availability. Analyze reliability. Support defensive research.**
