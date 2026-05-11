# FreshCart — E-Commerce Microservices Platform

> **Angular 21** storefront · **Spring Cloud** backend ·

A full-stack e-commerce platform. The backend is a Spring Cloud microservice mesh with Netflix Eureka discovery and a unified API gateway. The frontend is an Angular 21 application with SSR, lazy-loaded routes, and guard-based admin separation.

---

## Architecture Overview

```
Angular 21 (SSR)  →  Spring Cloud Gateway (:8080)  →  Netflix Eureka (:8761)
                                  │
          ┌───────────────────────┼────────────────────────────┐
          │                       │                            │
    user-service (:8081)   product-service (:8082)    cart-service (:8083)
    MySQL · JWT · Mail     MongoDB · Catalog           MySQL · WebClient
                                                             │
                                                    order-service (:8084)
                                                    MySQL · Stripe · Kafka
                                                             │
                                                   wishlist-service (:8085)
                                                   MySQL · OpenFeign
```

All client traffic enters through the **Spring Cloud Gateway**. Services register with **Eureka** and are addressed via `lb://service-id` — no hardcoded URLs between services.

---

## Features

| Domain | What's built |
|--------|--------------|
| **Auth** | JWT issuance & validation, refresh flow, RBAC (`ROLE_USER` / `ROLE_ADMIN`) |
| **Users** | Registration, login, profile management, password reset via email (Mailtrap) |
| **Catalog** | Products, categories, brands, subcategories — MongoDB document model |
| **Cart** | Add/update/remove items; service-to-service via reactive WebClient |
| **Orders** | Cash orders + Stripe checkout sessions + verified webhooks |
| **Events** | Kafka order event producer for order lifecycle |
| **Wishlist** | Wishlist management; product data fetched via OpenFeign |
| **Admin** | Product, user, order, and content management — `@PreAuthorize` RBAC |
| **Frontend** | Angular 21 SSR, Tailwind CSS, Flowbite, lazy routes, auth/admin guards |

---

## Tech Stack

### Backend
- **Java 21** · Spring Boot · Spring Cloud (Eureka, Gateway)
- **Spring Security** + **JWT** (`jjwt 0.12.6`)
- **Spring Data JPA** (MySQL 8) + **Spring Data MongoDB** (MongoDB 7)
- **WebClient** (reactive inter-service calls) + **OpenFeign** (declarative inter-service calls)
- **Stripe Java SDK 25.3.0** — checkout sessions + webhook signature verification
- **Spring Boot Actuator** — health, info, gateway metrics
- **SpringDoc OpenAPI** — Swagger UI on order service
- **Spring Boot Mail** — password reset emails
- **AspectJ / AOP** — cross-cutting concerns
- **Lombok** · Jakarta Validation · Spring Boot DevTools

### Frontend
- **Angular 21** with SSR (`@angular/ssr` + Express 5)
- **Tailwind CSS 4** + Flowbite + Font Awesome + Swiper
- **RxJS** + Angular Signals
- **ngx-toastr** + **ngx-spinner**
- Route guards: `authGuard`, `adminGuard`
- **Vitest** for unit testing

### Databases
- **MySQL 8** — users, carts, orders, wishlists (per-service isolated DBs)
- **MongoDB 7** — product catalog (Atlas URI supported)

### Infrastructure
- **Docker** + **Docker Compose** — full stack orchestration
- **Mailtrap** — email delivery (dev/test)

---


## Quick Start (Docker)

**Prerequisites:** Docker & Docker Compose

```bash
# Clone the repo
git clone https://github.com/NadaAhmed159/E-Commerce
cd E-Commerce

# Start everything — databases, services, and frontend
docker compose up --build
```

| Service | URL |
|---------|-----|
| Frontend | http://localhost:4000 |
| Gateway | http://localhost:8080 |
| Eureka UI | http://localhost:8761 |

---

## Local Development (without Docker)

**Prerequisites:** Java 21, Node 20+, MySQL 8, MongoDB 7, (optional) Kafka, Stripe CLI

1. Start MySQL and MongoDB locally (see connection strings in each service's `application.yml`)
2. Start services in this order: `eurekaserver` → `apigateway` → domain services (any order)
3. Start the frontend:

```bash
cd E-commerceF
npm install
npm start    # Angular dev server — default port 4200
```

Set `environment.ts` → `baseUrl: 'http://localhost:8080'` to point at the gateway.

---

## API Routes (via Gateway on :8080)

| Path prefix | Service |
|-------------|---------|
| `/api/v1/auth/**` | user-service |
| `/api/v1/users/**` | user-service |
| `/api/v1/admin/**` | user-service |
| `/api/v1/products/**` | product-service |
| `/api/v1/categories/**` | product-service |
| `/api/v1/brands/**` | product-service |
| `/api/v1/cart/**` | cart-service |
| `/api/v1/orders/**` | order-service |
| `/api/v1/wishlist/**` | wishlist-service |

> Order service exposes **Springdoc OpenAPI** — visit `/swagger-ui.html` on port `8084` in local dev.

---

## Environment Variables


| Variable | Used by | Notes |
|----------|---------|-------|
| `JWT_SECRET` | All services | Shared signing secret |
| `SPRING_DATASOURCE_URL` | MySQL services | Per-service DB URL |
| `SPRING_DATASOURCE_USERNAME` | MySQL services | |
| `SPRING_DATASOURCE_PASSWORD` | MySQL services | |
| `MONGODB_URI` | product-service | Atlas URI or local |
| `EUREKA_HOST` / `EUREKA_PORT` | All services | Defaults: `localhost` / `8761` |

---

## Roadmap

- [ ] Align Spring Boot / Cloud versions across gateway (3.2.x) and domain services (4.0.x)
- [ ] Add Kafka broker + Zookeeper/KRaft to Docker Compose; document topics
- [ ] Move secrets to Vault / cloud secret manager; rotate JWT and DB credentials
- [ ] Add Flyway/Liquibase; disable `ddl-auto: update` for production
- [ ] Tighten CORS from `allowedOrigins: "*"` to specific production origins
- [ ] Implement or remove the `payment-service` gateway route (currently unresolved)
- [ ] Centralized tracing with OpenTelemetry / Zipkin
- [ ] Rate limiting and WAF at the gateway layer


