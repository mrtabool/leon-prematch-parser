# Leon Prematch Parser

A high-performance Spring Boot application designed to parse betting lines from Leon API with a focus on top-tier leagues and efficient data handling.

## Key Features
* **Concurrent Processing:** Uses a dedicated `ThreadPoolTaskExecutor` to process multiple matches in parallel.
* **Smart Logging:** Implements local buffering for logs to prevent console output interleaving (thread-safe formatting).
* **Robust API Client:** Handles session initialization and cookie management automatically.
* **Clean Architecture:** Fully decoupled DTOs, Services, and Async components.

## Prerequisites
* **Java 21** or higher
* **Maven 3.9+**