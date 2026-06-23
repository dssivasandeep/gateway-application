# Event Ledger - Gateway Service

## Overview

Gateway Service is the public-facing component of the Event Ledger system.

Responsibilities:

* Accept transaction events
* Validate requests
* Enforce idempotency
* Persist events
* Invoke Account Service
* Propagate Trace IDs
* Apply Circuit Breaker protection
* Expose event query APIs

---

## Technology Stack

* Java 8
* Spring Boot 2.7.18
* Spring Data JPA
* H2 Database
* Resilience4j
* Micrometer
* Maven

---

## Running the Application

### Prerequisites

* Java 8
* Maven 3.8+

### Start Application

```bash
mvn spring-boot:run
```

Application starts on:

```text
http://localhost:8081
```

---

## H2 Console

```text
http://localhost:8081/h2-console
```

JDBC URL:

```text
jdbc:h2:mem:eventdb
```

Username:

```text
sa
```

Password:

```text
(empty)
```

---

## API Endpoints

### Submit Event

```http
POST /events
```

### Get Event

```http
GET /events/{eventId}
```

### Get Events By Account

```http
GET /events?account={accountId}
```

### Health Check

```http
GET /health
```

### Metrics

```http
GET /actuator/metrics
```

---

## Features

### Idempotency

Duplicate event submissions do not create duplicate records or affect account balances.

### Event Ordering

Events are returned sorted by event timestamp regardless of arrival order.

### Circuit Breaker

Resilience4j Circuit Breaker protects the service when Account Service becomes unavailable.

### Trace Propagation

Gateway generates and propagates Trace IDs through the X-Trace-Id header.

---

## Testing

```bash
mvn test
```

Includes:

* Validation Tests
* Idempotency Tests
* Integration Tests
* Circuit Breaker Tests
* Trace Propagation Tests

---

## Dependency

Requires Account Service running on:

```text
http://localhost:8082
```
