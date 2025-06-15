# Sentinel – Distributed Event Monitoring System

A high-performance distributed backend system designed to process high-volume event streams, detect anomalies in real-time using sliding window logic, and provide scalable persistence.

![Architecture](https://via.placeholder.com/800x400?text=System+Architecture+Loading...)
*(Replace with actual architecture diagram if available)*

## 🚀 Key Features
- **High-Throughput Ingestion**: Kafka-based event streaming to handle bursts of traffic.
- **Real-Time Anomaly Detection**: Redis-based **Sliding Window** algorithm to detect pattern deviations with low latency.
- **Async Persistence**: Non-blocking writes to PostgreSQL for audit logs.
- **Scalable**: Dockerized microservices architecture.

## 🛠️ Tech Stack
- **Java 17** / **Spring Boot 3**
- **Apache Kafka** (Event Streaming)
- **Redis** (Sliding Window & Caching)
- **PostgreSQL** (Persistence)
- **Docker & Docker Compose**

## 🏃‍♂️ How to Run

### Prerequisites
- Docker & Docker Compose
- Java 17+ (for local dev)

### 1. Start Infrastructure
Start Kafka, Zookeeper, Redis, and Postgres:
```bash
docker-compose up -d
```

### 2. Run Application
```bash
./mvnw spring-boot:run
# OR if using local maven
mvn spring-boot:run
```

## 🧪 Testing & Validation
A Python script is included to simulate high-concurrency traffic and validate the sliding window logic.

```bash
# Install dependencies
pip install requests

# Run load test
python3 scripts/validate_logs.py
```

## 📡 API Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/events` | Ingest a new event payload |

## 📊 Monitoring
- **Kafka UI**: http://localhost:8080 (if configured)
- **Redis**: Port 6379

---
*Built by [Durga Pavan Kumar Pailla](https://github.com/Durgapavankumar)*
