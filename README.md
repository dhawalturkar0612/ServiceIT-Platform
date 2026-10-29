# 🛠️ ServiceIT - On-Demand Local Services Platform

> A full-stack monolithic web application connecting consumers with verified service providers for home and utility services, built with **Spring Boot** and **React (Vite)**.

---

## 📌 Project Overview

**ServiceIT** simplifies finding, booking, and managing local home services (e.g., plumbing, electrical, cleaning, maintenance). The platform features an end-to-end workflow: consumers search and book services with online payments, providers manage service offerings and appointments, and administrators oversee platform operations and audit trails.

---

## 🚀 Key Features

### 👤 Consumer
- **Browse & Search**: Filter service catalog by category, location, and rating.
- **Booking Management**: Book appointments, view service history, and track status.
- **Secure Payments**: Integrated with Razorpay for card, UPI, and net banking payments.
- **Account & Security**: Profile management, email verification, and password reset.

### 🧰 Service Provider
- **Service Listings**: Add, update, and manage services offered with custom pricing.
- **Booking Pipeline**: Accept, update, and complete assigned customer bookings.
- **Earnings & Dashboard**: Track completed jobs, performance, and schedule.

### 🛡️ Administrator
- **User & Provider Management**: Verify, activate, or deactivate users and service providers.
- **Service Catalog Management**: Create and manage service categories and offerings.
- **Payment & Booking Oversight**: Monitor platform transactions and refund requests.
- **System Audit Logs**: Track critical administrative and security events.

---

## 💻 Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Frontend** | React 18, Vite, React Router, Context API, CSS3 / Modern UI |
| **Backend** | Spring Boot 3, Spring Security 6, Spring Data JPA, Hibernate |
| **Database** | MySQL 8.0 |
| **Authentication** | JWT (JSON Web Token) with Role-Based Access Control (RBAC) |
| **Payment Gateway** | Razorpay API |
| **Mailing Service** | Spring Mail (SMTP / Gmail) |
| **API Documentation** | Swagger / OpenAPI 3 |
| **Build Tools** | Maven (Backend), npm / Vite (Frontend) |

---

## 📂 Project Structure

```text
ServiceIT_CDAC_Monolithic/
├── backendserver/                     # Spring Boot Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/backend/
│   │   │   │   ├── config/            # Security, CORS, OpenAPI, Razorpay configs
│   │   │   │   ├── controllers/       # REST API endpoints
│   │   │   │   ├── dtos/              # Data Transfer Objects
│   │   │   │   ├── entities/          # JPA Hibernate entities
│   │   │   │   ├── repositories/      # Spring Data JPA interfaces
│   │   │   │   └── services/          # Business logic implementations
│   │   │   └── resources/
│   │   │       └── application.properties
│   ├── pom.xml
│   └── mvnw / mvnw.cmd
│
├── frontendMonolithic/                # React (Vite) Application
│   ├── src/
│   │   ├── components/                # Reusable UI components
│   │   ├── context/                   # Auth & Theme state management
│   │   ├── layouts/                   # Admin, Consumer, Provider, and Public layouts
│   │   ├── pages/                     # Role-specific dashboard & auth views
│   │   ├── routes/                    # Protected and role-based routes
│   │   └── services/                  # Axios / Fetch API integrations
│   ├── package.json
│   └── vite.config.js
│
├── .gitignore
└── README.md
```

---

## ⚙️ Getting Started & Setup

### Prerequisites
- **JDK 17 or 21** installed
- **Node.js** (v18 or higher) & **npm** installed
- **MySQL Server 8.0+** running locally
- **IDE**: Spring Tool Suite (STS) / IntelliJ IDEA / VS Code

---

### 1. Database Setup
Log into your MySQL shell and create the database:
```sql
CREATE DATABASE serviceit_db;
```
*(The schema and tables will be automatically created on startup via `spring.jpa.hibernate.ddl-auto=update`).*

---

### 2. Backend Setup (Spring Boot)

1. Open a terminal or your IDE in `backendserver/`.
2. Configure your local environment variables in your IDE or system:

| Variable Name | Description | Example / Default |
| :--- | :--- | :--- |
| `DB_PASSWORD` | MySQL root user password | `root` or your local password |
| `JWT_SECRET` | 256-bit secret key for JWT tokens | *Your JWT base64 secret* |
| `RAZORPAY_KEY_ID` | Razorpay test key ID | `rzp_test_...` |
| `RAZORPAY_KEY_SECRET` | Razorpay secret key | *Your Razorpay secret* |
| `MAIL_USERNAME` | Gmail address for notifications | `example@gmail.com` |
| `MAIL_PASSWORD` | 16-character Google App Password | `xxxx xxxx xxxx xxxx` |

3. Run the backend:
```bash
cd backendserver

# Using Maven Wrapper (Windows)
mvnw.cmd spring-boot:run

# Using Maven Wrapper (macOS/Linux)
./mvnw spring-boot:run
```
The backend server will start on **`http://localhost:8080`**.

4. **API Documentation**: Once running, access the interactive Swagger UI at:
   ```text
   http://localhost:8080/swagger-ui/index.html
   ```

---

### 3. Frontend Setup (React + Vite)

1. Navigate to the frontend directory:
```bash
cd frontendMonolithic
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

4. Open your browser and navigate to:
```text
http://localhost:5173
```

---

## 🔒 Security & Best Practices
- **Role-Based Authorization**: Routes and endpoints are guarded based on user roles (`ADMIN`, `CONSUMER`, `SERVICE_PROVIDER`).
- **Encrypted Credentials**: Sensitive credentials (passwords, JWT secrets, gateway keys) are injected via environment variables and never checked into source control.
- **Audit Logging**: Comprehensive audit trail for sensitive administrative and state changes.

---

## 👨‍💻 Developed By

**Dhawal Turkar**
- GitHub: [@aka-obito](https://github.com/aka-obito)
- CDAC Post Graduate Diploma Project (Solo Developer)