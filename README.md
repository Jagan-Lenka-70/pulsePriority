# PulsePriority - Hospital Emergency Management System

PulsePriority is a full-stack Java Spring Boot project that simulates how hospitals can prioritize emergency patients using a **Max Priority Queue (Heap concept)** instead of normal FIFO order.

The system is designed for high-pressure environments where patients with higher triage severity must be treated first. It includes both:
- a secure backend API,
- and a modern frontend dashboard built with Thymeleaf + Bootstrap.

---

## Project Highlights

- **Priority-based triage queue** (higher score = higher treatment priority)
- **Doctor Console** to extract the next highest-priority patient
- **Live queue view** and **treated history log**
- **One-click demo data initializer** for instant classroom demo
- **Emergency level badges**
  - `8-10` -> Red (Critical)
  - `4-7` -> Yellow (Urgent)
  - `1-3` -> Green (Stable)
- **Secure login** using Spring Security
- **Persistent data layer** using Spring Data JPA + H2
- **Responsive dark-blue medical UI** for class/demo presentation

---

## Tech Stack

- Java 17
- Spring Boot 3
- Spring MVC + Thymeleaf
- Spring Security
- Spring Data JPA
- H2 Database
- Bootstrap 5 + Bootstrap Icons

---

## Folder Structure

```text
PulsePriority/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/pulsepriority/hospitalemergency/
    │   │   ├── config/
    │   │   ├── controller/
    │   │   ├── dto/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   ├── service/
    │   │   └── PulsePriorityApplication.java
    │   └── resources/
    │       ├── application.properties
    │       ├── templates/
    │       │   ├── login.html
    │       │   └── index.html
    │       └── static/css/styles.css
    └── test/java/com/pulsepriority/hospitalemergency/
```

---

## How to Run (After Downloading ZIP from GitHub)

### 1) Download and extract
1. Open your GitHub repository page.
2. Click **Code -> Download ZIP**.
3. Extract the ZIP to any folder.

### 2) Install prerequisites (any system)
- **Java 17+** installed
- Verify:
  - `java -version`

### 3) Open terminal in project root
Go inside extracted folder where `pom.xml` exists.

### 4) Run the application (no global Maven required)
```bash
./mvnw spring-boot:run
```

On Windows (PowerShell/CMD):

```bat
mvnw.cmd spring-boot:run
```

### 5) Open in browser
- App Dashboard: [http://localhost:8080](http://localhost:8080)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### 6) Login credentials
- Username: `doctor`
- Password: `doctor123`

### 7) Demo data behavior
- First startup auto-loads sample emergency patients.
- Disable it by setting `app.demo-data.enabled=false` in `src/main/resources/application.properties`.

---

## API Endpoints

- `POST /api/triage/patients` - Register a patient
- `GET /api/triage/queue` - Get waiting queue sorted by priority
- `POST /api/triage/next` - Treat next highest-priority patient
- `GET /api/triage/history` - Get treated patients log

Sample request:

```json
{
  "fullName": "Anita Verma",
  "triageScore": 9,
  "symptoms": "Severe chest pain"
}
```

---

## How the Priority Logic Works

PulsePriority follows a Max Heap style rule:
- Larger triage score means higher urgency.
- Queue operations are organized to always serve the most critical patient first.
- This models real emergency workflows better than FIFO.

---

## Project Explanation (For Viva / Presentation)

PulsePriority demonstrates the practical use of data structures in healthcare operations. The core idea is to map emergency severity scores to a priority queue so that treatment order is clinically optimized.

The backend is built with Spring Boot and exposes secure endpoints for triage operations. Spring Security ensures only authenticated staff can manage patient flow. Spring Data JPA with H2 handles persistence and state tracking. The frontend dashboard gives a clean real-time view of queue status, emergency severity, and treatment history, making the system both technically sound and easy to operate.

This project combines:
- **algorithmic efficiency** (`O(log n)` style priority behavior),
- **enterprise backend architecture**,
- and **user-friendly medical UI**.

---

## Screenshots

Add screenshots to `docs/screenshots/` with the names below:

- `01-login.png`
- `02-dashboard.png`
- `03-priority-queue.png`
- `04-treated-history.png`

Then they will render here:

![Login Screen](docs/screenshots/01-login.png)
![Dashboard](docs/screenshots/02-dashboard.png)
![Priority Queue](docs/screenshots/03-priority-queue.png)
![Treated History](docs/screenshots/04-treated-history.png)

---

## Future Improvements

- JWT-based authentication with staff roles
- PostgreSQL/MySQL production database
- Real-time updates via WebSocket
- Audit logging and report export
