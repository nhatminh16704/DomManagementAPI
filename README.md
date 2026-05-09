# DomManagementAPI (DomHub)

## System Overview

DomHub is a comprehensive RESTful API backend designed for a Dormitory Management System. It provides a centralized infrastructure to manage student accommodations, facility operations, billing, and internal communications within a university or independent dormitory environment.

## Technology Stack

- **Core Framework:** Spring Boot 3.4.3 (Java 17)
- **Data Access:** Spring Data JPA (Hibernate)
- **Database:** MySQL
- **Security:** Spring Security with JSON Web Token (JWT) Authentication
- **Build Tool:** Maven
- **External Integrations:** VNPay Payment Gateway

## System Architecture & Modules

The system is logically partitioned into several core domains to handle different aspects of dormitory operations:

### 1. Identity & Access Management (IAM)

- **Role-Based Access Control (RBAC):** Supports distinct user roles such as `Admin`, `Staff`, and `Student`.
- **Authentication:** Secures endpoints using JWT. Users authenticate to receive a token which must be presented in the Authorization header for subsequent API requests.
- **Account Management:** Handles user provisioning, credentials mapping, and profile management for both students and operational staff.

### 2. Facility & Inventory Management

- **Building Infrastructure:** Hierarchical modeling of the facility, divided into `Blocks` (Buildings), `TypeRoom` (Room categories based on capacity/amenities), and individual `Rooms`.
- **Asset Tracking:** Manages an inventory of `Devices` (e.g., fans, air conditioners, beds) and tracks their allocation to specific rooms via `DeviceRoom` mappings.

### 3. Accommodation & Rental Lifecycle

- **Registration Periods:** Admins can define specific timeframes (`RegistrationPeriod`) during which students can apply for dormitory rooms.
- **Room Rentals:** Manages the entire lifecycle of a student's stay (`RoomRental`), from initial room assignment to contract termination, ensuring capacity constraints and validity periods are respected.

### 4. Billing & Financial Operations

- **Invoicing:** Generation and management of `RoomBill` entities covering rent and utility expenses.
- **Payment Gateway Integration:** Integrated natively with **VNPay**, allowing students to securely pay their dormitory bills online. The system handles payment callbacks and automatically updates invoice statuses.

### 5. Operations & Incident Management

- **Maintenance Reporting:** Students can submit `Reports` for broken appliances, plumbing issues, or general maintenance requests. Staff can track and update the resolution status of these reports.
- **Disciplinary Actions:** Staff and Admins can log `Violations` against students for breaching dormitory regulations, keeping an auditable history of infractions.
- **Dashboard & Analytics:** Provides aggregated metrics and statistics for administrative oversight.

### 6. Internal Communication

- **Notifications:** A broadcast system (`Notification`) to send announcements and critical alerts to users.
- **Messaging:** Direct messaging (`Message`) capabilities allowing communication between students and staff/administration for inquiries and support.

## Database Design Highlights

The system utilizes a relational model (MySQL) heavily leveraging JPA mappings for complex relationships:

- One-to-Many mappings for building hierarchies (Blocks -> Rooms).
- Many-to-Many associations using explicit join entities (e.g., `DeviceRoom` with composite keys) to accurately track asset distributions.
- Secure relationship mapping between Accounts and their respective profiles (Student/Staff).
