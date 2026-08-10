# 🛋️ FurniHub - Full Stack Integration Guide

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     FurniHub Platform                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────┐    HTTP/REST API    ┌─────────────────┐  │
│  │   Frontend   │ ◄─────────────────► │    Backend      │  │
│  │   React.js   │    localhost:3000   │  Spring Boot    │  │
│  │              │                     │  localhost:8080 │  │
│  └──────┬───────┘                     └────────┬────────┘  │
│         │                                      │            │
│         │                                      │            │
│  ┌──────▼──────────────────────────────────────▼────────┐  │
│  │                    MySQL Database                     │  │
│  │                  localhost:3306                       │  │
│  │              Database: e_commerce                     │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                             │
└─────────────────────────────────────────────────────────────┘

Customer Flow:                  Admin Flow:
Login → Catalog → Cart → Order   Login → /admin → Dashboard → Modules
      (Port 3000)                     (Port 3000, ADMIN role)
```

## Unified Startup

### Prerequisites
- **MySQL 8.0+** running on `localhost:3306`
- **Java 17+** installed
- **Node.js 18+** installed

### Step 1: Database Setup
```bash
# Option A: Using setup script
setup-database.bat

# Option B: Manual
mysql -u root -p < database/schema.sql
```

### Step 2: Start Backend
```bash
cd backend
.\mvnw spring-boot:run
# Backend starts on http://localhost:8080/api
```

### Step 3: Start Frontend
```bash
cd frontend
npm install
npm start
# Frontend starts on http://localhost:3000
```

### Step 4: Access Application
```
Customer Site:  http://localhost:3000
Admin Panel:    http://localhost:3000/admin
API Docs:       http://localhost:8080/api
```

## Database Schema Overview

| Table | Purpose |
|-------|---------|
| `users` | Customer & Admin accounts |
| `jwt_tokens` | Active JWT sessions |
| `otp_verification` | Password reset OTPs |
| `categories` | Product categories |
| `products` | Product catalog |
| `productimages` | Product image URLs |
| `cart_items` | Shopping cart |
| `orders` | Customer orders |
| `order_items` | Order line items |
| `coupons` | Discount coupons |
| `reviews` | Product reviews |

## Shared Services

### Authentication (Both Customer & Admin)
- **POST** `/api/auth/register` — Customer registration
- **POST** `/api/auth/login` — Unified login (returns role)
- **POST** `/api/auth/logout` — Server-side token invalidation + cookie clearing
- **POST** `/api/auth/forgot-password` — OTP-based password reset
- **POST** `/api/auth/verify-otp` — OTP verification
- **POST** `/api/auth/reset-password` — Password reset
- **POST** `/api/auth/change-password` — Change password (authenticated)

### Customer Catalog (Public)
- **GET** `/api/categories` — Browse categories
- **GET** `/api/products` — Browse products
- **GET** `/api/products/{id}` — Product details

### Admin Management (ADMIN Role Required)
- **GET** `/api/admin/dashboard/stats` — Dashboard KPIs
- **CRUD** `/api/admin/products` — Product management
- **CRUD** `/api/admin/categories` — Category management
- **CRUD** `/api/admin/users` — User management
- **CRUD** `/api/admin/orders` — Order management
- **CRUD** `/api/admin/inventory` — Stock management
- **Analytics** `/api/admin/analytics` — Business analytics
- **CRUD** `/api/admin/coupons` — Coupon management
- **CRUD** `/api/admin/reviews` — Review moderation
- **Settings** `/api/admin/settings` — Admin profile
- **Notifications** `/api/admin/notifications` — Notifications

## Authentication Flow

```
Customer/Admin Login
        │
        ▼
  POST /api/auth/login
        │
        ▼
  Backend validates credentials
        │
        ▼
  Generate JWT token
        │
        ▼
  Store in DB (jwt_tokens table)
        │
        ▼
  Return token + role
        │
        ▼
  Frontend stores in localStorage
        │
        ▼
  Frontend includes in Authorization header
  OR Backend sets HTTP-only cookie (auth_token)
        │
        ▼
  Subsequent requests authenticated
```

## Role-Based Access Control

| Feature | Customer | Admin |
|---------|----------|-------|
| View Products | ✅ | ✅ |
| View Categories | ✅ | ✅ |
| Register/Login | ✅ | ✅ |
| Cart & Orders | ✅ | ❌ |
| Product Management | ❌ | ✅ |
| User Management | ❌ | ✅ |
| Order Management | ❌ | ✅ |
| Inventory Management | ❌ | ✅ |
| Analytics | ❌ | ✅ |
| Coupons | ❌ | ✅ |
| Reviews | ❌ | ✅ |

## Security Features

1. **JWT Authentication** — Stateless token-based auth
2. **Role-Based Access** — ADMIN vs CUSTOMER roles
3. **HTTP-Only Cookies** — XSS protection
4. **Password Encryption** — BCrypt hashing
5. **Server-Side Logout** — Token deletion from DB
6. **CORS Configuration** — Origin whitelisting
7. **SQL Injection Prevention** — JPA parameterized queries
8. **Input Validation** — Jakarta validation annotations

## Development Workflow

### Backend Development
```bash
cd backend
.\mvnw spring-boot:run
# Hot reload enabled with Spring Boot DevTools
```

### Frontend Development
```bash
cd frontend
npm start
# Hot reload enabled with React Scripts
# Proxied API calls to localhost:8080
```

### Database Changes
1. Update `database/schema.sql`
2. Run `setup-database.bat`
3. Update JPA entities if needed

## Production Deployment

### Docker Deployment
```bash
docker-compose up --build -d
```

### Manual Deployment
1. Build backend: `cd backend && mvnw clean package`
2. Build frontend: `cd frontend && npm run build`
3. Deploy JAR to server
4. Serve frontend `build/` with Nginx
5. Configure MySQL database
6. Set environment variables

## Troubleshooting

### Port Already in Use
```bash
# Change ports in application.properties (backend)
# Or use different port when starting frontend
npm start -- --port 3001
```

### Database Connection Failed
```bash
# Verify MySQL is running
# Check credentials in application.properties
# Ensure database exists: CREATE DATABASE e_commerce;
```

### CORS Errors
```bash
# Frontend origin must be in CorsConfig.java allowedOrigins
# Or use proxy in package.json for development
```

### Token Not Persisting
```bash
# Check browser localStorage for 'token' key
# Verify cookie settings in application.properties
# Ensure backend sends Set-Cookie header
```
