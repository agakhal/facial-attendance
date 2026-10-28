# FaceMetric: Webcam Attendance System

A full-stack web app for tracking attendance. Students register with a webcam photo and check in with their student ID. Attendance is logged with timestamps and shown on a live dashboard.

**Live demo:** https://facial-attendance-vlca.onrender.com/

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
- **Build and deployment:** Maven, Docker, hosted on Render

## API Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/FaceMetrics/signup` | Register a student (ID, name, photo) |
| POST | `/api/attendance/checkin` | Log a check-in |
| GET | `/api/attendance/records` | List attendance records |

## Roadmap

- Face verification at check-in: compare the live photo to the registered photo using Deep Java Library (DJL) with PyTorch
- Authentication for the dashboard

## Running Locally

**Prerequisites:** Java 17 and a running MySQL instance.

```bash
git clone https://github.com/agakhal/facial-attendance.git
cd facial-attendance
```

1. Create a MySQL database named `facemetric`.
2. Set these environment variables before running:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/facemetric
export SPRING_DATASOURCE_USERNAME=your_mysql_username
export SPRING_DATASOURCE_PASSWORD=your_mysql_password
```

On Windows (PowerShell), use `$env:SPRING_DATASOURCE_URL="..."` instead of `export`.

3. Run:

```bash
./mvnw spring-boot:run
```

Open `http://localhost:8080` and allow camera access when prompted.

**With Docker:**

```bash
docker build -t facemetric .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/facemetric \
  -e SPRING_DATASOURCE_USERNAME=your_mysql_username \
  -e SPRING_DATASOURCE_PASSWORD=your_mysql_password \
  facemetric
```

## Author

Aavleen Gakhal: [LinkedIn](https://www.linkedin.com/in/aavleen-gakhal/)
