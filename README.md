# FaceMetric: Webcam Attendance System

A full-stack web app for tracking attendance. Students register with a webcam photo and check in with their student ID. Attendance is logged with timestamps and shown on a live dashboard.

**Live demo:** https://facial-attendance-vlca.onrender.com/

> The demo runs on a free hosting tier, so the first load after a period of inactivity can take up to a minute while the server wakes up.

## Features

- **Student signup:** enter your ID and name and capture a photo with your webcam
- **Check-in:** enter your student ID and capture a photo to log attendance
- **Duplicate check-in prevention:** repeat check-ins within a 5-minute window are rejected
- **Pacific-time timestamps** (America/Vancouver) on every record
- **Attendance dashboard** with student ID, name, time, and status

## Tech Stack

- **Backend:** Java 17, Spring Boot 3.3, Spring Web (REST), Spring Data JPA
- **Database:** MySQL
- **Frontend:** HTML, CSS, vanilla JavaScript (MediaDevices API for the webcam, Canvas API for capture)
- **Build and deployment:** Maven, Docker, hosted on Render with a hosted MySQL database configured through environment variables

## API Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/FaceMetrics/signup` | Register a student (ID, name, photo) |
| POST | `/api/attendance/checkin` | Log a check-in |
| GET | `/api/attendance/records` | List attendance records |

## Current Limitations

- **No face matching yet.** The check-in photo is captured and sent to the server, but it is not compared to the registered photo. Check-ins are currently verified by student ID only. The "facial recognition" and "verify your identity" wording on the site describes the planned feature (see Roadmap), not current behavior.
- **The dashboard is public.** Anyone with the link can view attendance records.
- **Photos are stored** in the database. Please don't use real student data with the demo.

## Roadmap

- [ ] Face verification at check-in: compare the live photo to the registered photo using Deep Java Library (DJL) with PyTorch (the dependency is already included)
- [ ] Authentication for the dashboard

## Running Locally

**Prerequisites:** Java 17 and a running MySQL instance.

1. Clone the repo:
   ```bash
   git clone https://github.com/agakhal/facial-attendance.git
   cd facial-attendance
   ```
2. Create a MySQL database named `facemetric`.
3. Set these environment variables before running:
   ```bash
   export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/facemetric
   export SPRING_DATASOURCE_USERNAME=your_mysql_username
   export SPRING_DATASOURCE_PASSWORD=your_mysql_password
   ```
   On Windows (PowerShell), use `$env:SPRING_DATASOURCE_URL="..."` instead of `export`.
4. Run:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Open http://localhost:8080 and allow camera access when prompted.

### With Docker

```bash
docker build -t facemetric .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/facemetric \
  -e SPRING_DATASOURCE_USERNAME=your_mysql_username \
  -e SPRING_DATASOURCE_PASSWORD=your_mysql_password \
  facemetric
```

## Author

[Aavleen Gakhal](https://www.linkedin.com/in/aavleen-gakhal/)
