# Enterprise Portal — Frontend Application

<div align="center">

[![📖 System Overview](https://img.shields.io/badge/📖_System-Overview-gray?style=for-the-badge)](../README.md)
[![🎨 Frontend Guide](https://img.shields.io/badge/🎨_Frontend-React_19-2ea44f?style=for-the-badge)](#enterprise-portal--frontend-application)
[![🔐 Security & JWT](https://img.shields.io/badge/🔐_Security-JWT_%26_RBAC-8957e5?style=for-the-badge)](../README.md#security--authentication-architecture)
[![⚡ RESTful API](https://img.shields.io/badge/⚡_REST_API-Endpoints-f0883e?style=for-the-badge)](../README.md#restful-api-design--implementation)
[![⚖️ MIT License](https://img.shields.io/badge/⚖️_License-MIT-gray?style=for-the-badge)](../LICENSE)

</div>

This directory houses the client-side single-page application (SPA) for **Enterprise Portal**, built with **React 19** and bundled with **Vite**.

---

## Architecture & Technology Stack

| Technology | Purpose |
|---|---|
| **React 19** | Component-driven declarative UI architecture |
| **Vite** | Lightning-fast development server with Hot Module Replacement (HMR) & production bundler |
| **CSS3 (Custom Design System)** | Pure CSS responsive glassmorphism/enterprise layout (`index.css`) without heavyweight runtime frameworks |
| **Fetch API & JWT** | Centralized REST client with automatic Bearer token injection and session recovery |

---

## Key Frontend Features & Design Patterns

### 1. Centralized REST Client & Auth Interceptor
All HTTP communication passes through the unified `request(path, options)` utility in `App.jsx`:
- **Bearer Token Injection:** Automatically attaches `Authorization: Bearer <token>` from `localStorage` to every request.
- **Session Expiry Guard:** Intercepts `401 Unauthorized` or `403 Forbidden` responses, invalidates state, clears storage, and redirects to the Login screen.
- **Unified Error Handling:** Parses backend `ErrorDetails` validation errors and presents user-friendly error banners.

### 2. Adaptive Role-Based Access Control (RBAC) UI
The UI dynamically reacts to the authenticated user's role (`ADMIN`, `MANAGER`, `EMPLOYEE`):
- **Dynamic Tabs:** The **Manager Approvals** hub is strictly mounted for `ADMIN` and `MANAGER` roles.
- **Action Guards:** Interactive action buttons (such as Approve/Reject actions, Equipment status changes, Announcement creation) are guarded based on permissions.
- **Self-Approval Prevention:** Approval controls are disabled for records submitted by the logged-in manager to prevent conflicts of interest.

### 3. Visual Meeting Room Scheduler & Collision Detection
- Renders an interactive 09:00 - 18:00 timeline grid for all corporate meeting rooms.
- Uses React `useMemo` to perform real-time collision detection against existing reservations, preventing conflicting bookings before sending requests to the server.

### 4. Collapsible Department Tree View
- Renders the corporate organizational chart into collapsible departmental branches.
- Highlights department managers at the top of each branch, followed by team members.
- Supports instant client-side filtering across employee names, departments, roles, and skills.

---

## Directory Structure

```
frontend/
├── src/
│   ├── assets/        # Visual assets, icons, and hero illustrations
│   ├── App.jsx        # Root application, views, state management, and API calls
│   ├── index.css      # Core responsive styling and theme variables
│   └── main.jsx       # React DOM entry point
├── public/            # Static assets
├── index.html         # HTML template
├── package.json       # Dependencies and npm scripts
└── vite.config.js     # Vite build and server configuration
```

---

## Getting Started

### 1. Install Dependencies
```bash
npm install
```

### 2. Run Development Server
```bash
npm run dev
```

The application will be running at `http://localhost:5173`.

### 3. Production Build
```bash
npm run build
```

Generates optimized static assets in the `dist/` folder ready for deployment.
