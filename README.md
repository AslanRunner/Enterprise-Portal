# Enterprise Portal

Enterprise Portal is a comprehensive, full-stack corporate operations and employee management system designed to centralize and streamline internal organizational workflows. The platform consolidates workforce directory management, meeting room reservations, leave processing, hardware inventory tracking, administrative approval workflows, and internal communications into a single unified interface.

The system is engineered on a layered three-tier architecture, pairing a robust **Spring Boot** RESTful API with a responsive **React (Vite)** single-page application and a **PostgreSQL** relational database.

---

## Application Showcase

### Executive Dashboard & Operations Hub
![Executive Dashboard](./docs/screenshots/dashboard.png)
*Central overview providing real-time room occupancies, birthdays, personal leave balances, equipment request statuses, and an interactive team appreciation board.*

### Manager Approvals Portal (Admin & Manager Governance)
![Manager Approvals](./docs/screenshots/manager-approvals.png)
*Unified administrative queue for reviewing, approving, and rejecting leave and equipment requests with built-in self-approval prevention.*

### Visual Meeting Room Scheduler & Conflict Prevention
![Meeting Room Scheduler](./docs/screenshots/meeting-rooms.png)
*Interactive timeline displaying real-time slot occupancy across rooms with client and server-side collision validation.*

### Company & Personal Leave Management
![Leave Management](./docs/screenshots/leaves.png)
*Multi-tab interface providing personal submission tracking and company-wide leave schedules with automatic weekend exclusion.*

### Corporate Announcements & Team Collaboration
![Announcements](./docs/screenshots/announcements.png)
*Threaded communication feed for company updates, interactive comments, and post reactions.*

---

## Architecture Overview

The application follows a standard three-tier architecture with clean separation of concerns:

```
┌────────────────────────────────────────────────────────┐
│                   React (Vite) SPA                     │  ← Presentation Layer
│   State Management, Dynamic Routing, Adaptive RBAC UI  │
└───────────────────────────┬────────────────────────────┘
                            │ HTTP / REST (JSON + JWT)
┌───────────────────────────▼────────────────────────────┐
│              Spring Boot REST Controllers              │  ← API Layer
│   JWT Security Filters & Stateless Authorization       │
│   Service Layer (Business Rules & Conflict Checkers)   │
└───────────────────────────┬────────────────────────────┘
                            │ Spring Data JPA / Hibernate
┌───────────────────────────▼────────────────────────────┐
│                  PostgreSQL Database                   │  ← Persistence Layer
└────────────────────────────────────────────────────────┘
```

**Request Lifecycle:** Every client request is intercepted by a custom JWT authentication filter. Upon token validation and role verification, requests are routed to specific controllers that delegate business logic to the service layer. The service interacts with Spring Data JPA repositories, enforcing domain constraints before data is persisted or mapped to response DTOs.

---

## Role-Based Access Control (RBAC) & Governance

The platform features an adaptive Role-Based Access Control model enforced both at the UI layer and at API endpoint boundaries:

| Capability | Employee | Manager | Admin |
|------------|:--------:|:-------:|:-----:|
| Personal Dashboard & Profile Management | Yes | Yes | Yes |
| Team Directory Search & Filtering | Yes | Yes | Yes |
| Meeting Room Reservation (Book/Update/Cancel own) | Yes | Yes | Yes |
| Submit Leave & Equipment Requests | Yes | Yes | Yes |
| Post Announcements & Comments | Comments only | Comments only | Yes |
| Access **Manager Approvals** Hub | No | **Yes** | **Yes** |
| Approve / Reject Leave Requests | No | **Yes** | **Yes** |
| Approve / Reject Hardware Requests | No | **Yes** | **Yes** |
| View Company-Wide Leave Rosters | No | **Yes** | **Yes** |
| View Company-Wide Hardware Inventory | No | **Yes** | **Yes** |

### Key Governance & Validation Rules
- **Dynamic Navigation:** When authenticated as an `ADMIN` or `MANAGER`, the navigation bar dynamically unlocks administrative views, including the dedicated **Manager Approvals** portal.
- **Self-Approval Prevention:** Managers and administrators are strictly prohibited from approving their own leave or equipment requests (`You cannot approve your own request`). These records must be handled by another qualified manager or administrator.
- **Dual-Layer Meeting Conflict Checking:** Room reservations undergo continuous client-side validation (`useMemo`) to disable submission during overlapping time frames, accompanied by strict server-side overlap checks before database persistence.
- **Working Day Leave Calculator:** Leave requests automatically calculate billable working days by filtering out weekend days (Saturday and Sunday) in real time.
- **Cascading Integrity:** Deleting corporate announcements triggers database cascades (`orphanRemoval = true`), safely eliminating orphan discussion comments without manual intervention.

---

## Core Modules

### 1. Dynamic Team Directory (Hierarchical Tree View)
Presents organizational departments as collapsible hierarchical trees. Department managers are highlighted with distinct accent styling at the top of each branch, followed by team personnel. Supports instant client-side filtering across names, departments, roles, and technical skills.

### 2. Meeting Room Scheduler
Provides visual timeline grids indicating room availability hour-by-hour (09:00 - 18:00). Users can select vacant time slots to pre-fill booking modals, inspect meeting objectives, and modify or cancel existing reservations they own.

### 3. Leave Management & Approval Engine
Enables staff to request Annual, Casual, or Sick leave. Requests route directly to the manager approval queue. Standard employees view personal request histories, while managers access comprehensive organizational leave schedules.

### 4. Equipment Provisioning & Hardware Lifecycle
Tracks company hardware through distinct lifecycle states (`IN STORAGE`, `ASSIGNED`, `RETURNED`, `FAULTY`). Employees can request devices with justifications, and return assigned equipment directly from their profile.

### 5. Corporate Announcements & Appreciation Board
Facilitates organization-wide updates with threaded comments and likes. Features an interactive appreciation widget on the dashboard allowing team members to publicly recognize peers.

### 6. Profile & Security Center
Allows personnel to manage personal data (email, date of birth, technical skill tags, and profile photo uploads with database TEXT-encoding support) alongside self-service password updates with current password verification.

---

## Tech Stack

| Layer | Technology | Description |
|-------|-----------|-------------|
| Backend Framework | Spring Boot 3.x | REST API, Dependency Injection, Security Configuration |
| Security | Spring Security & JWT | Stateless token-based authentication and authorization |
| Persistence | Spring Data JPA / Hibernate | Object-Relational Mapping (ORM) and schema auto-generation |
| Database | PostgreSQL 15 | Relational database storage |
| API Documentation | Swagger / OpenAPI | Interactive API specification at `/swagger-ui.html` |
| Frontend Framework | React 19 | Component architecture with hooks-based state management |
| Build Tool (Frontend) | Vite | Fast HMR dev server and optimized production bundling |
| Build Tool (Backend) | Maven | Dependency management and build lifecycle |
| Boilerplate Reducer | Lombok | Automatic generation of getters, setters, and constructors |

---

## Prerequisites

Ensure the following tools are installed before running the project:

- **Java 17** or higher (JDK)
- **Node.js 18** or higher (with npm)
- **Docker** & **Docker Compose** *(Recommended for instant database setup)*

---

## Getting Started

### Step 1 — Launch the Database (Docker)

The project includes a ready-to-use `docker-compose.yml` file configuring PostgreSQL:

```bash
docker-compose up -d
```

*Default credentials:* Port `5432`, Username `postgres`, Password `123`, Database `postgres`.

### Step 2 — Start the Backend (Spring Boot)

From the project root directory:

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

The server initializes on `http://localhost:8080`. Hibernate will automatically generate all database schemas on startup. Access Swagger documentation at `http://localhost:8080/swagger-ui.html`.

### Step 3 — Start the Frontend (React / Vite)

Open a separate terminal window:

```bash
cd frontend
npm install
npm run dev
```

The application will be accessible at `http://localhost:5173`.

---

## Project Directory Structure

```
enterprise-portal/
├── src/main/java/com/aslan/
│   ├── config/          # Security filters, CORS, and password encoders
│   ├── controller/      # REST API endpoints
│   ├── dto/             # Data Transfer Objects
│   ├── entity/          # JPA database entities
│   ├── enums/           # System enumerations (LeaveStatus, LeaveType, etc.)
│   ├── exception/       # Custom business exceptions
│   ├── handler/         # Global REST exception handler
│   ├── jwt/             # JWT token provider, filter, and authentication DTOs
│   ├── repository/      # Spring Data JPA repositories
│   └── service/         # Business logic implementation
├── frontend/
│   └── src/
│       ├── App.jsx      # Core single-page application & module views
│       ├── index.css    # Custom responsive design system
│       └── main.jsx     # Frontend entry point
├── docs/
│   └── screenshots/     # Application screenshots for documentation
├── docker-compose.yml   # PostgreSQL container specification
├── pom.xml              # Maven dependencies & plugins
└── README.md
```

---

## API Documentation

When the backend application is running, the full interactive OpenAPI specification is accessible via Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## Contributing

1. Fork this repository.
2. Create a feature branch: `git checkout -b feature/NewFeature`
3. Commit your changes: `git commit -m 'Add NewFeature'`
4. Push to branch: `git push origin feature/NewFeature`
5. Submit a Pull Request.

---

## License

This project was developed as an enterprise-grade full-stack portal for operational management and portfolio demonstration.
