# Campus Queue Management System Backend

* **Course**: INSY 8322 – Web Technology and Internet
* **Institution**: Adventist University of Central Africa (AUCA)
* **Student Name**: MOUANDZA Handy Christopher
* **Student ID**: 27284
* **Project Name**: Campus Queue Management System

---

## 1. Project Overview

The **Campus Queue Management System** replaces physical queues at university service offices with virtual tickets. 
A student takes a queue ticket that is served by a staff member sitting at a counter; each staff member is assigned to several campus services (such as Registration, Finance, Library Support). 
The ticket moves from `WAITING` to `CALLED` and ends `SERVED` or `SKIPPED`. Once a ticket is served, the staff member records service logs describing the action taken and time spent.

This backend application is built using:
* **Java 17**
* **Spring Boot 3.2.5** (Spring Web, Spring Data JPA)
* **PostgreSQL**
* **Maven**

---

## 2. Requirements & Architecture

* **Part A (Domain Model & CRUD)**:
  * Entities: `CampusService`, `Counter`, `StaffMember`, `Student`, `QueueTicket`
  * Enum: `TicketStatus` (`WAITING`, `CALLED`, `SERVED`, `SKIPPED`)
  * Full CRUD endpoints under `/api/{entity-path}/...`
* **Part B (Derived Queries)**:
  * `B1`: Students by lastName (IgnoreCase, OrderBy firstName Asc) -> `/api/students/by-last-name?lastName=...`
  * `B2`: Queue tickets by status (OrderBy ticketDate Asc) -> `/api/queue-tickets/by-status?status=...`
  * `B3`: Queue tickets between dates -> `/api/queue-tickets/between?start=...&end=...`
* **Part C (JPQL Queries with Joins)**:
  * `C1`: Staff members by campus service (JOIN on collection, LOWER) -> `/api/staff-members/by-campus-service?name=...`
  * `C2`: Free counters (NOT EXISTS subquery) -> `/api/counters/free`
  * `C3`: Campus services nobody holds (IS EMPTY) -> `/api/campus-services/unused`
  * `C4`: Students of a staff member (DISTINCT, path navigation) -> `/api/students/of-staff-member/{staffMemberId}`
* **Part D (Business Rules)**:
  * `D1`: Safe saving (no past dates, single active ticket per staff per day, auto-WAITING status)
  * `D2`: Controlled status flow transitions (`WAITING` -> `CALLED`/`SKIPPED` -> `SERVED`/`SKIPPED`)
  * `D3`: Assign campus service to staff member
  * `D4`: Assign staff member to another counter (with conflict prevention)
* **Part E (ServiceLog Entity)**:
  * Entity: `ServiceLog` (table `service_log`)
  * `E1`: Write service log for SERVED ticket
  * `E2`: Service log history of a student (`/api/service-logs/student/{regNumber}`)
  * `E3`: Delete log & prevent deleting queue tickets that have service logs
* **Part F (Reports & Bulk Updates)**:
  * `F1`: Tickets per status count (GROUP BY, COUNT) -> `/api/queue-tickets/stats/by-status`
  * `F2`: Cancel day in bulk for absent staff member (JPQL `@Modifying UPDATE`) -> `/api/queue-tickets/cancel-day`
* **Bonus (+10 Marks)**:
  * Bonus 1: Dashboard summary (`/api/dashboard/summary`) returning counts of students, staff members, free counters, tickets today, waiting tickets, and service logs.
  * Bonus 2: Paginated queue tickets (`/api/queue-tickets/page?page=0&size=5&sort=ticketDate,desc`).

---

## 3. Database Configuration

Ensure PostgreSQL is running locally on port `5432`. Create the database:

```sql
CREATE DATABASE campus_queue_db;
```

Update `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/campus_queue_db
spring.datasource.username=postgres
spring.datasource.password=postgres123
```

---

## 4. How to Run the API

1. Open a terminal in the project root:
   ```bash
   mvn clean compile
   ```
2. Start the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   The application starts on `http://localhost:8080`.
   Initial seed data (counters, campus services, staff members, students, tickets, and logs) is automatically loaded on first startup.

---

## 5. Testing with Postman

1. Open Postman.
2. Click **Import** and select the file:
   `campus-queue-management-system-midterm.postman_collection.json`
3. Execute the requests organized by requirements:
   * **Part A**: CRUD operations & 404 tests.
   * **Part B**: Derived queries (B1, B2, B3).
   * **Part C**: JPQL queries (C1, C2, C3, C4).
   * **Part D**: Business rules (D1 past date 400, D2 invalid transition 400, D4 conflict 409).
   * **Part E**: Service logs (E1 non-served 400, E2 history, E3 delete protect 409).
   * **Part F**: Reports (F1 count stats, F2 bulk cancellation).
   * **Bonus**: Dashboard summary and Pageable tickets.
