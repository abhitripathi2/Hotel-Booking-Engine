# Hotel Booking Engine 

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-008CDD?style=for-the-badge&logo=stripe&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

A robust, backend-oriented RESTful API for hotel reservations built with modern Java and Spring Boot technologies. This core backend service handles real-time room inventory management, dynamic price calculation, secure payment processing, and role-based user authentication.

## 🚀 Key Features & Engineering Highlights

*   **Dynamic Pricing Engine (Strategy Pattern):** Architected a flexible pricing system utilizing the Strategy Design Pattern. Room rates are calculated dynamically at checkout based on Surge, Occupancy, Holiday, and Urgency pricing strategies.
*   **Concurrency & Inventory Control:** Implemented Pessimistic Locking on the PostgreSQL database to help prevent concurrent booking conflicts and double-booking during high-traffic reservation windows.
*   **Secure Payment Integration:** Integrated Stripe API for checkout sessions, accompanied by a dedicated Webhook listener to asynchronously capture and confirm payment status updates independent of client-side connectivity.
*   **Role-Based Access Control (RBAC):** Secured the API using stateless JWT (JSON Web Tokens). Configured a custom Auth Filter and Web Security configurations to restrict endpoints based on Guest, Hotel Manager, and Admin roles.
*   **Global Exception Handling:** Designed a clean, standardized error response structure using `@RestControllerAdvice` to ensure predictable API contracts for frontend consumers.
*   **Comprehensive API Documentation:** Integrated OpenAPI 3.0 / Swagger UI for seamless frontend consumption and API testing.

## 🏗️ Architecture & Core Modules

This project follows a strict multi-layered architecture (Controller, Service, Repository, DTO, Entity) with clear separation of concerns.

*   **Auth & User Module:** Handles user registration, login, JWT refresh tokens, profile updates, and guest list management.
*   **Browse & Search Module:** Features a paginated API allowing users to search available properties efficiently.
*   **Booking & Payment Module:** Manages the lifecycle of a reservation, updating Inventory tables, attaching guests, capturing Stripe payments, and transitioning booking statuses (e.g., INIT, CONFIRMED, CANCELLED).
*   **Administration Module:** Protected full-CRUD endpoints for hotel managers to onboard properties, configure room types, update inventory, and generate revenue/occupancy reports.

## 🔌 API Endpoints (Selected Highlights)

The API is fully documented via OpenAPI/Swagger. Below is a comprehensive overview of the core capabilities:

### Authentication & User Management
| HTTP Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/signup` | Register a new user | Public |
| `POST` | `/api/v1/auth/login` | Authenticate and receive JWT | Public |
| `POST` | `/api/v1/auth/refresh` | Issue new JWT access tokens | Public |
| `GET` | `/api/v1/users/profile` | Fetch authenticated user profile | Protected |
| `PATCH` | `/api/v1/users/profile` | Update user profile details | Protected |
| `GET` | `/api/v1/users/myBookings` | View user's booking history | Protected |
| `POST` | `/api/v1/users/guests` | Add a new guest to user's guest list | Protected |

### Browsing & Search
| HTTP Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/hotels/search` | Paginated hotel search based on criteria | Public |
| `GET` | `/api/v1/hotels/{hotelId}/info` | Fetch detailed hotel information | Public |

### Booking Engine & Checkout
| HTTP Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/bookings/init` | Initialize a booking and lock inventory | Guest |
| `POST` | `/api/v1/bookings/{bookingId}/addGuests` | Attach registered guests to booking | Guest |
| `POST` | `/api/v1/bookings/{bookingId}/payments`| Process Stripe checkout session | Guest |
| `POST` | `/api/v1/bookings/{bookingId}/cancel` | Cancel reservation & release inventory | Guest |
| `POST` | `/api/v1/webhook/payment` | Stripe webhook listener for payment capture | Stripe API |

### Hotel Administration (Role: ADMIN / HOTEL_MANAGER): _Protected endpoints for hotel managers to manage properties, rooms, inventory, and reports._
| HTTP Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/admin/hotels` | Onboard a new hotel property | Admin |
| `PATCH` | `/api/v1/admin/hotels/{hotelId}/activate` | Activate a property for public viewing | Admin |
| `GET` | `/api/v1/admin/hotels/{hotelId}/reports` | Generate revenue and occupancy reports | Admin |
| `POST` | `/api/v1/admin/hotels/{hotelId}/rooms` | Add new room configurations | Admin |
| `PUT` | `/api/v1/admin/hotels/{hotelId}/rooms/{roomId}`| Update specific room details | Admin |
| `PATCH` | `/api/v1/admin/inventory/rooms/{roomId}`| Manually adjust room inventory/status | Admin |

## ⚙️ Local Setup & Installation

**1. Prerequisites**
*   Java 21 or higher
*   PostgreSQL running locally (default port 5433)
*   Stripe CLI (for webhook testing)

**2. Environment Variables**
This application requires sensitive credentials to be injected securely. Configure the following environment variables in your IDE or system path before running:

```properties
DB_USERNAME=your_postgres_user
DB_PASSWORD=your_postgres_password
JWT_SECRET=your_generated_random_jwt_secret_string
STRIPE_SECRET_KEY=sk_test_your_stripe_secret_key
STRIPE_WEBHOOK_SECRET=whsec_your_stripe_webhook_secret
```

**3. Database Initialization**
The application uses Hibernate `ddl-auto=update` to automatically generate the relational schema on the first run. Create an empty PostgreSQL database and configure the database name in `spring.datasource.url`.

**4. Running Stripe Webhooks Locally**
To test payments locally, forward Stripe events to the local webhook controller using the Stripe CLI:

```bash
stripe listen --forward-to localhost:8080/api/v1/webhook/payment --all-snapshot
```