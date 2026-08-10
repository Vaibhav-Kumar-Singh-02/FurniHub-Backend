# 🛋️ FurniHub - Furniture E-Commerce Platform

A full-stack furniture e-commerce application built with **React.js** (Frontend), **Java Spring Boot** (Backend), and **MySQL** (Database). Includes both **Customer** and **Admin** interfaces.

## 🚀 Quick Start

### Prerequisites
- **Java 17+**
- **Node.js 18+** & npm
- **MySQL 8.0+**
- **Maven 3.8+** (or use included `mvnw`)

### Option 1: Docker (Recommended)

```bash
# Start all services with Docker Compose
docker-compose up --build

# Access:
# Frontend: http://localhost:3000
# Backend: http://localhost:8080/api
# MySQL: localhost:3306
```

### Option 2: Local Development

#### 1. Database Setup
```bash
# Start MySQL service
# Then run the schema:
mysql -u root -p < database/schema.sql
```

#### 2. Configure Backend (if needed)
If your MySQL password is different from `vaibhav`, edit `backend/src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_PASSWORD
```

#### 3. Start Backend
```bash
cd backend
mvn spring-boot:run
# OR on Windows:
# mvnw spring-boot:run
```

#### 4. Start Frontend
```bash
cd frontend
npm install
npm start
```

#### 5. One-Click Startup (Windows)
```bash
double-click start.bat
```

## 🔗 URLs

| Service | URL |
|---------|-----|
| **Customer Frontend** | http://localhost:3000 |
| **Admin Dashboard** | http://localhost:3000/admin |
| **Backend API** | http://localhost:8080/api |
| **MySQL** | localhost:3306 |

## 👤 Default Admin Credentials

| Field | Value |
|-------|-------|
| Email | `admin@furnihub.com` |
| Password | `admin123` |
| Role | ADMIN |

## 🏗️ Architecture

```
FurniHub/
├── backend/                 # Spring Boot REST API
│   ├── src/main/java/
│   │   └── com/furnihub/
│   │       ├── controller/  # REST Controllers
│   │       ├── service/     # Business Logic
│   │       ├── repository/  # Data Access
│   │       ├── entity/      # JPA Entities
│   │       ├── dto/         # Data Transfer Objects
│   │       ├── config/      # Security, JWT Config
│   │       └── enums/       # Enums
│   └── src/main/resources/
│       └── application.properties
├── frontend/                # React Application
│   ├── src/
│   │   ├── components/      # Reusable Components
│   │   ├── pages/           # Page Components
│   │   │   ├── admin/       # Admin Dashboard Pages
│   │   │   └── ...          # Customer Pages
│   │   ├── services/        # API Services
│   │   ├── styles/          # CSS Styles
│   │   └── App.js           # Routing
│   └── package.json
├── database/
│   └── schema.sql           # MySQL Schema
└── docker-compose.yml       # Docker Orchestration
```

## 🔐 Authentication & Authorization

### Customer Flow
1. Register/Login at `/login` or `/register`
2. JWT token stored in HTTP-only cookie (`auth_token`)
3. Access catalog, cart, orders, profile

### Admin Flow
1. Login with ADMIN role credentials
2. Access `/admin` dashboard
3. Manage products, users, orders, inventory, analytics

### Security Features
- JWT-based authentication
- Role-based access control (ADMIN vs CUSTOMER)
- HTTP-only cookies for token storage
- CORS configuration
- Password encryption with BCrypt
- Server-side session invalidation on logout

## 📊 Admin Modules

| Module | Features |
|--------|----------|
| **Dashboard** | KPI cards, revenue charts, recent orders/customers |
| **Products** | CRUD, search, filter, pagination, multiple images |
| **Categories** | CRUD operations |
| **Users** | View, edit, block/unblock, delete, restore |
| **Orders** | View, update status, cancel, refund |
| **Inventory** | Stock management, low stock alerts |
| **Analytics** | Daily/Monthly/Yearly/Overall analytics |
| **Coupons** | Create, update, enable/disable, delete |
| **Reviews** | Approve, reject, reply, delete |
| **Settings** | Profile edit, change password |
| **Notifications** | View, mark as read, generate |

## 🛒 Customer Features

- Product browsing with search & filter
- Product detail view
- Category-based navigation
- User registration & authentication
- Password reset via OTP
- Cart management
- Order placement
- Profile management

## 🔧 Configuration

### Backend (`application.properties`)
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/e_commerce
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

# JWT
jwt.secret=YourSecretKey
jwt.expiration=2592000000

# Server
server.port=8080
```

### Frontend (`.env`)
```env
REACT_APP_API_URL=http://localhost:8080/api
```

## 🐳 Docker Commands

```bash
# Start all services
docker-compose up --build

# Stop all services
docker-compose down

# View logs
docker-compose logs -f

# Reset database
docker-compose down -v
```

## 📝 API Documentation

### Auth Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login user |
| POST | `/api/auth/logout` | Logout user |
| POST | `/api/auth/forgot-password` | Request OTP |
| POST | `/api/auth/verify-otp` | Verify OTP |
| POST | `/api/auth/reset-password` | Reset password |
| POST | `/api/auth/change-password` | Change password |
| GET | `/api/auth/validate` | Validate token |

### Customer Catalog Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | Get all categories |
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |

### Admin Endpoints (Requires ADMIN Role)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/dashboard/stats` | Dashboard statistics |
| GET/POST/PUT/DELETE | `/api/admin/products/**` | Product management |
| GET/POST/PUT/DELETE | `/api/admin/categories/**` | Category management |
| GET/PUT/DELETE | `/api/admin/users/**` | User management |
| GET/PUT/POST | `/api/admin/orders/**` | Order management |
| GET/POST | `/api/admin/inventory/**` | Inventory management |
| GET | `/api/admin/analytics/**` | Analytics |
| GET/POST/PUT/DELETE | `/api/admin/coupons/**` | Coupon management |
| GET/POST/DELETE | `/api/admin/reviews/**` | Review management |
| GET/PUT | `/api/admin/settings/**` | Admin settings |
| GET/POST | `/api/admin/notifications/**` | Notifications |

## 🧪 Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 📦 Tech Stack

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- MySQL 8.0
- JWT (JSON Web Tokens)
- Lombok
- Maven

### Frontend
- React 18
- React Router DOM v6
- Axios
- React Icons
- CSS3 with Custom Properties

## 👥 Roles & Permissions

| Role | Access |
|------|--------|
| **CUSTOMER** | Browse products, manage cart, place orders, view own profile |
| **ADMIN** | Full access to admin dashboard, manage all entities, view analytics |

## 📄 License

This project is proprietary software developed for FurniHub.

## 🤝 Support

For issues or questions, contact the development team.
