# ✈️ Flight Service API

Welcome to the **Flight Service API** — a modern, scalable backend solution built with **Spring Boot** to manage flight-related operations. From booking flights to handling real-time notifications and user roles, this API is designed to serve as a strong backbone for any airline or travel platform.

---

## 🌍 Overview

The Flight Service API supports a full suite of features, including:

* Flight and booking management
* Secure user authentication and authorization
* Real-time updates and notifications
* Scalable architecture with future-ready enhancements

This document walks you through its features, setup, and technical design.

---

## ✨ Features

### ✅ Current Functionalities

**🛫 Flight Management**

* Create, retrieve, update, and delete flights
* Search by flight number, airline, airport
* Manage schedules, pricing, and seating

**📑 Booking System**

* Create and fetch bookings
* Link bookings with flights and users

**👥 User Management**

* Register and log in securely
* Role-based access control
* Profile and preferences management

**🔐 Security Layer**

* Spring Security + JWT with RSA
* Custom filters (`authFilter`, `tokenFilter`)
* Robust access control mechanisms

---

### 🧭 Future Roadmap

**🔍 Advanced Flight Search**

* Flexible dates, stops, class filters
* Airline, time-of-day, and layover filtering

**🕒 Real-Time Status & Notifications**

* WebSocket & webhook support
* Email, SMS, and push alerts

**💰 Dynamic Pricing Engine**

* Price adjustments by demand and timing
* Competitor price monitoring
* Fare rules (e.g., refundability)

**💼 Ancillary Services**

* Baggage, seat upgrades, in-flight meals
* Travel insurance and lounge booking

**💳 Payment Gateway Integration**

* Multi-method payments (cards, wallets)
* PCI DSS compliance
* Refund and cancellation handling

**✈️ Loyalty Programs**

* Miles redemption
* Tier-based benefits

**💡 Personalization**

* Fare alerts, saved preferences
* Travel history analysis

**🌐 Globalization**

* Multi-language support
* Currency conversions

**🛡️ Operational Excellence**

* Rate limiting, caching, logging
* API versioning and centralized error handling

**🧠 Smart Features**

* Predictive delay analytics
* Carbon emission estimations
* NLP-powered travel assistant chatbot

---

## 🛠️ Tech Stack

* **Backend:** Spring Boot 3.x
* **Language:** Java 17
* **Database:** MySQL
* **ORM:** Spring Data JPA
* **Security:** Spring Security, JWT, bcrypt
* **Build Tool:** Maven
* **Validation:** Jakarta Bean Validation
* **Testing:** JUnit 5, Mockito
* **Extras:** Lombok, DevTools, Jackson

---

## 🚀 Getting Started

### 🧩 Prerequisites

* Java 17+
* Maven 3+
* MySQL
* Git
* OpenSSL (for RSA keys)

### 📦 Installation

```bash
git clone https://github.com/oussama166/flight-service-api.git
cd flight-service-api
mvn clean install
mvn spring-boot:run
```

### 🗂️ Database Configuration

Edit `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:8889/JetBlue
    username: root
    password: root
```

The DB will be created automatically on launch.

---

## 🔐 JWT & RSA Key Setup

The API uses RSA keys for signing JWT tokens. Follow these steps to generate and configure the keys:

1️⃣ Generate Private Key

```bash
openssl genrsa -out private.pem 2048
```

2️⃣ Extract Public Key

```bash
openssl rsa -in private.pem -pubout -out public.pem
```

3️⃣ Place the Keys

```
src/main/resources/certs/private.pem
src/main/resources/certs/public.pem
```

4️⃣ Configure Application

Update your `application.yaml` or `application.properties`:

```yaml
jwt:
  private-key-path: classpath:certs/private.pem
  public-key-path: classpath:certs/public.pem
```

Now your Spring Boot app can sign and verify JWTs using RSA encryption.

---

## 📡 JWT & RSA Key Architecture

Here’s how JWT and RSA keys fit into the API security:


[![](https://img.plantuml.biz/plantuml/svg/RPFBJiD034NtynLMLo2Akiy2qOAAU4cevR7fPaYirkemKso7g7-FyoWYQPTpxJc-SvrSYthMyYiu9Gq83n-lS06BvHIcfMumAkLUAKMOXrGvvtDD33U1h09UO3JZL0aoJE0vVLB0aNCx0r9EEsyjDUGcHUKK0dAljCIyPDBzd88YIqCxQD29gtZcZT2Ung1fYx4zd8y6CFwX79_ythoqf4Q0HkdtpagXY29FQKrv-uXEWKQoeVD0wusEOPRIbk9w36_r6pkcs6pNHN1vDNWYP0qamWA_InGrM6bte__2oSwBL8cBSpUuSufoXn7PAqgzabLSqZeALAGRU67wiKApf4VSdu1rvh9ccwzrQFyD3uFFJ8b5CwSTC7sv1sxjYnjsdmIy-q35mIo8UVLQCgvYQxZy39RqmUz9uh_rPJ3DyteRTsphmiA-hLkCX8LplJwxrrzb1lF4bUVY4Bxhruy7BL1sAGeUWVyI_EjmrnQI_IU_)](https://editor.plantuml.com/uml/RPFBJiD034NtynLMLo2Akiy2qOAAU4cevR7fPaYirkemKso7g7-FyoWYQPTpxJc-SvrSYthMyYiu9Gq83n-lS06BvHIcfMumAkLUAKMOXrGvvtDD33U1h09UO3JZL0aoJE0vVLB0aNCx0r9EEsyjDUGcHUKK0dAljCIyPDBzd88YIqCxQD29gtZcZT2Ung1fYx4zd8y6CFwX79_ythoqf4Q0HkdtpagXY29FQKrv-uXEWKQoeVD0wusEOPRIbk9w36_r6pkcs6pNHN1vDNWYP0qamWA_InGrM6bte__2oSwBL8cBSpUuSufoXn7PAqgzabLSqZeALAGRU67wiKApf4VSdu1rvh9ccwzrQFyD3uFFJ8b5CwSTC7sv1sxjYnjsdmIy-q35mIo8UVLQCgvYQxZy39RqmUz9uh_rPJ3DyteRTsphmiA-hLkCX8LplJwxrrzb1lF4bUVY4Bxhruy7BL1sAGeUWVyI_EjmrnQI_IU_)


* **Private RSA Key:** Used only by the server to **sign JWT**.
* **Public RSA Key:** Used by the server to **verify JWT** in security filters.

This ensures secure, tamper-proof authentication for all API requests.

---

## 📡 API Endpoints

### ✈️ Flight

* `POST /setFlight` → Add a new flight
* `GET /getFlight/{flightNumber}` → Get details by flight number
* `GET /getFlightByArrDes` → Search flights by departure, arrival, and status

### 👤 User

* `POST /register` → Sign up a new user
* `POST /login` → Authenticate and receive JWT

### 📘 Booking

* `POST /setBooking` → Make a new booking

**Interactive API docs:** Available via `/swagger-ui.html`

---

## 🏗️ Architecture

### 📐 Layered Design

* **Controller:** Handles requests (`org.jetblue.jetblue.Controller`)
* **Service:** Business logic (`org.jetblue.jetblue.Service`)
* **Repository:** Data layer using JPA (`org.jetblue.jetblue.Repositories`)
* **Model:** Entities, DTOs, enums (`org.jetblue.jetblue.Models`)
* **Security:** JWT & filters (`org.jetblue.jetblue.Filters`)

### 📊 Design Patterns

* Dependency Injection
* DTO + Mapper Pattern
* Repository Abstraction
* Layered Separation of Concerns

---

## 🧪 Testing

### 🔎 Test Categories

* **Unit Tests:** Services (`Service/Implementation/`)
* **Controller Tests:** API endpoints
* **HTTP Test Files:** Manual request samples

### ▶️ Run Tests

```bash
mvn test                     # Run all
mvn test jacoco:report       # With coverage
mvn test -Dtest=UserImplTest
```

Frameworks:

* JUnit 5
* Mockito
* Spring Security Test

---

## 🤝 Contributing

We welcome contributors! Please:

1. Fork and create a new branch
2. Follow clean code and naming conventions
3. Add or update tests
4. Submit a well-documented pull request

---

## ⚖️ License

Distributed under the **Apache 2.0 License**. See [`LICENSE`](LICENSE) for details.

---

## 📬 Contact

Maintainer: **Oussama Ouardi**
📧 [oussamaouardi80@gmail.com](mailto:oussamaouardi80@gmail.com)
🐙 [GitHub](https://github.com/oussama166)
🔗 [Project Link](https://github.com/oussama166/flight-service-api)

---

Let your API fly high! 🚀✈️
*Ready for takeoff? Clone, build, and start exploring today.*
