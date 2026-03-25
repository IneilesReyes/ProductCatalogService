# Product Catalog Service

A REST microservice responsible for serving the product catalog, built as part of a microservices architecture migration. It supports discount calculation, category filtering, sorting, and pagination.

---

## Tech Stack

- **Java 21**
- **Spring Boot**
- **Maven**
- **PostgreSQL**
- **Docker / Docker Compose**

---

## Architecture

The service follows **Hexagonal Architecture (Ports & Adapters)** combined with **Domain-Driven Design (DDD)** principles, organized into three main layers:

```
src/
├── application/        # Use cases, ports (interfaces), exceptions
├── domain/             # Business logic, models, domain services
└── infrastructure/     # Controllers, persistence, mappers, config
```

### Key Architectural Decisions

#### Hexagonal Architecture
The domain and application layers are completely isolated from infrastructure concerns. Spring, JPA, and PostgreSQL details live exclusively in the infrastructure layer, making the core business logic framework-agnostic and independently testable.

#### Strategy Pattern for Discounts
Discount calculation is implemented using the **Strategy design pattern**. Each discount rule is its own independent implementation of `DiscountCalculatorStrategy`. The `DiscountSelector` domain service collects all applicable strategies, picks the one with the highest discount, and applies it to the product — without any conditional logic in the use case.

This makes the system fully open/closed: new discount rules can be added by simply creating a new strategy class and registering it as a bean, without touching any existing code.

Current discount rules:

| Rule | Discount |
|---|---|
| Category: `Electronics` | 15% |
| Category: `Home & Kitchen` | 25% |
| SKU ending in `5` (e.g. `SKU0005`) | 30% |

Only one discount applies per product — the highest one takes priority.

#### Separate Category Table
Categories are stored in their own `category` table, with a foreign key relationship to the `product` table. This allows adding or renaming categories without any code changes, keeping the system flexible and data-driven.

---

## Running the Project

### Prerequisites

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

### Steps

```bash
# 1. Clone the repository
git clone <repository-url>
cd <repository-folder>

# 2. Build and start the services
docker compose up --build
```

This will spin up two containers:
- **db** — PostgreSQL instance with the `productcatalogdb` database
- **app** — The Spring Boot application on port `8081`

The app waits for the database to be healthy before starting, and automatically initializes the schema and seed data on first run.

### Stop the services

```bash
docker compose down
```

---

## API

### Base URL

```
http://localhost:8081/product-catalog-service
```

### Endpoints

#### Get Product Catalog

```
GET /v1/products
```

**Query Parameters**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `category` | `string` | No | Filter products by category name. If omitted, returns all products. |
| `page` | `int` | No | Page number (default: `0`) |
| `size` | `int` | No | Page size (default: `20`) |
| `sort` | `string` | No | Sort field and direction (e.g. `price,asc`) |

Sortable fields: `sku`, `price`, `description`, `category`

**Example Requests**

```bash
# All products
curl "http://localhost:8081/product-catalog-service/v1/products"

# Filter by category
curl "http://localhost:8081/product-catalog-service/v1/products?category=Electronics"

# Paginated and sorted
curl "http://localhost:8081/product-catalog-service/v1/products?page=0&size=5&sort=price,asc"

# Combined
curl "http://localhost:8081/product-catalog-service/v1/products?category=Electronics&page=0&size=5&sort=price,desc"
```

**Example Response**

```json
{
    "content": [
        {
            "description": "Aromatherapy Essential Oil Diffuser",
            "listPrice": 16.50,
            "category": "Home & Kitchen",
            "discountApplied": 4.13,
            "finalPrice": 12.37,
            "sku": "SKU0026"
        },
        {
            "description": "Ceramic Coffee Mug, 350ml",
            "listPrice": 12.50,
            "category": "Home & Kitchen",
            "discountApplied": 3.13,
            "finalPrice": 9.37,
            "sku": "SKU0010"
        },
        {
            "description": "LED Desk Lamp with Adjustable Brightness",
            "listPrice": 23.50,
            "category": "Home & Kitchen",
            "discountApplied": 5.88,
            "finalPrice": 17.62,
            "sku": "SKU0021"
        },
        {
            "description": "Stainless Steel Cutlery Set, 24 Pieces",
            "listPrice": 18.00,
            "category": "Home & Kitchen",
            "discountApplied": 4.50,
            "finalPrice": 13.50,
            "sku": "SKU0013"
        },
        {
            "description": "Stainless Steel Water Bottle, 1L",
            "listPrice": 29.50,
            "category": "Home & Kitchen",
            "discountApplied": 7.38,
            "finalPrice": 22.12,
            "sku": "SKU0003"
        }
    ],
    "page": {
        "size": 20,
        "number": 0,
        "totalElements": 5,
        "totalPages": 1
    }
}
```

**Error Responses**

| Status | Scenario |
|---|---|
| `400 Bad Request` | Invalid or unknown category provided |
| `400 Bad Request` | Invalid value type for a query parameter (e.g. wrong type for `page` or `size`) |
| `503 Service Unavailable` | Database connection failed or persistence error |
| `500 Internal Server Error` | Unexpected error |

All error responses follow this structure:

```json
{
  "message": "Invalid or unknown category - category: Electronics123",
  "timestamp": "2026-03-24T15:27:00.994407513"
}
```

---

## Running Tests

```bash
./mvnw test
```
