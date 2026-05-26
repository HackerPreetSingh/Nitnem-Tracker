# ੴ Nitnem Tracker ੴ

A production-ready Telegram-based Nitnem tracking application built using Java, Spring Boot, PostgreSQL, and Docker.

The application helps users track their daily Nitnem progress, maintain consistency, monitor long-term completion goals, and manage personalized spiritual targets through an interactive Telegram bot interface.

---

# ✨ Features

## User Features

* Create custom Nitnems
* Update daily Nitnem progress
* Delete Nitnems
* View all active Nitnems
* Track today's progress
* Track cumulative progress till date
* Duration-based tracking
* Validation-driven conversational workflow
* Telegram reply keyboard support
* Multi-user support
* Persistent PostgreSQL storage
* Cloud deployment ready

---

# 🚀 Planned Features

* Predefined Nitnem templates
* Unit normalization support

    * Raw Count
    * Maala-based input
* Mool Mantar conversion support
* Waheguru Simran conversion support
* Analytics dashboard
* REST APIs
* React frontend
* Webhook migration
* Progress indicators
* Daily reminders
* Entry editing
* Statistics and streaks
* PWA/mobile-friendly frontend

---

# 🛠️ Tech Stack

## Backend

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Telegram Bots API
* Gradle
* Lombok

## Database

* Supabase PostgreSQL

## ☁️ Deployment

* Docker
* Koyeb / Render compatible

---

# 🧱 Project Architecture

The application follows a layered architecture:

```text
Telegram Bot
    ↓
Command Service
    ↓
State Handlers
    ↓
Services
    ↓
Repositories
    ↓
PostgreSQL
```

---

# 🔄 Current Workflow Design

The bot follows a state-machine-driven conversational architecture.

Each user interaction transitions through explicit states.

Example:

```text
IDLE
→ WAITING_FOR_NITNEM_NAME
→ WAITING_FOR_TARGET_COUNT
→ WAITING_FOR_DURATION
→ SAVE
```

---

# 📁 Project Structure

```text
src/main/java/com/nitnem/tracker
│
├── bot
├── config
├── controller
├── entity
├── model
├── repository
├── service
├── state
│   └── handler
├── utils
└── validation
```

---

# ⚙️ Core Modules

## State Handlers

Responsible for:

* User interaction flow
* State transitions
* Input validation orchestration
* Session updates
* Delegating business logic to services

---

## Services

Contain reusable business logic.

Examples:

* NitnemService
* NitnemEntryService
* ValidationService
* TelegramCommandService
* UserSessionService

---

## Repositories

Spring Data JPA repositories responsible for database interaction.

---

# 🗄️ Database Design

## User

Stores Telegram user information.

### Important Fields

* id
* telegramChatId

---

## Nitnem

Stores user-created Nitnem goals.

### Important Fields

* id
* name
* targetCount
* durationDays
* startDate
* active
* unitConversionFactor
* user

---

## NitnemEntry

Stores daily completion entries.

### Important Fields

* id
* completedCount
* date
* doneAt
* nitnem

---

# 🔢 Unit Conversion Design

The system internally stores normalized raw counts.

Example:

```text
2 Maalas × 108 = 216 raw count
```

This design enables:

* Easier analytics
* Consistent calculations
* Flexible input methods
* Future extensibility

---

# ✅ Validation System

Centralized validation architecture used.

Examples:

* Positive integer validation
* Logical range validation
* Nitnem name validation
* Duplicate Nitnem checks
* Today count sanity checks

---

# 🧠 Session Management

Currently:

* In-memory conversational session management
* State-driven workflow tracking

Future improvements planned:

* Persistent session storage
* Distributed session handling
* Redis-backed sessions

---

# Deployment

## Environment Variables

The application uses environment variables for sensitive configuration.

### Required Variables

```env
SPRING_PROFILES_ACTIVE=dev
NITNEM_BOT_TOKEN=YOUR_TOKEN
DB_URL=jdbc:postgresql://YOUR_DB_URL
DB_USERNAME=YOUR_DB_USERNAME
DB_PASSWORD=YOUR_DB_PASSWORD
```

---

# 💻 Running Locally

## Clone Repository

```bash
git clone <repo-url>
```

---

## Build Application

```bash
./gradlew clean build
```

---

## Run Application

```bash
./gradlew bootRun
```

---

# 🐳 Docker Support

## Build Docker Image

```bash
docker build -t nitnem-tracker .
```

---

## Run Docker Container

```bash
docker run -p 8080:8080 \
-e NITNEM_BOT_TOKEN=YOUR_TOKEN \
-e DB_URL='jdbc:postgresql://YOUR_DB_URL' \
-e DB_USERNAME=YOUR_DB_USERNAME \
-e DB_PASSWORD=YOUR_DB_PASSWORD \
nitnem-tracker
```

---

# Docker Cleanup Commands

## Remove All Containers

```bash
docker rm -f $(docker ps -aq)
```

---

## Remove Unused Images and Cache

```bash
docker system prune -a -f
```

---

# 🤖 Telegram Bot Flow

## Create Nitnem

```text
/create
→ Enter Nitnem Name
→ Enter Nitnem Unit
→ Enter Target Count
→ Enter Duration
→ Save Nitnem
```

---

## Update Nitnem

```text
/update
→ Select Nitnem
→ Select Entry Unit
→ Enter Count
→ Save Entry
```

---

## Delete Nitnem

```text
/delete
→ Select Nitnem
→ Delete Nitnem and Entries
```

---

# 🏗️ Future Architectural Improvements

## Webhook Migration

Currently using polling.

Planned migration:

```text
Polling → Webhook
```

Benefits:

* Lower resource usage
* Better scalability
* Faster updates
* Cleaner cloud architecture

---

# 🌐 Planned REST API Layer

Future backend APIs:

* Create Nitnem
* Update Entries
* Fetch Analytics
* Dashboard APIs
* User APIs

This will support:

* React frontend
* Mobile frontend
* Third-party integrations

---

# ⚛️ React Frontend Plan

Future frontend stack:

* React
* Responsive UI
* PWA support
* Mobile-friendly design
* Dashboard analytics

---

# 🧩 Known Challenges Solved

## Circular Dependency Issues

Resolved through:

* Service responsibility separation
* Repository-level access
* Avoiding unnecessary bidirectional service coupling

---

## Duplicate User Entry Issues

Mitigated using:

* Validation
* Workflow confirmations
* Planned duplicate-message suppression

---

## Telegram Conversational Complexity

Handled through:

* Explicit state management
* Dedicated handlers
* Centralized session management

---

# 🔐 Security Practices

* Secrets stored using environment variables
* No hardcoded credentials
* External managed PostgreSQL
* Dockerized runtime

---

# 🎯 Why This Project Matters

This project demonstrates:

* Backend architecture design
* State-machine-driven workflows
* Spring Boot production practices
* PostgreSQL integration
* Dockerization
* Telegram Bot development
* Validation architecture
* Cloud deployment readiness
* Scalable backend thinking

---

# 📚 Learning Outcomes

Key backend engineering concepts explored:

* Stateful workflows
* Session management
* Docker fundamentals
* Cloud deployment
* PostgreSQL integration
* JPA relationships
* Validation architecture
* Service layering
* Conversational UX challenges
* Scalability considerations
* Polling vs Webhooks

---

# 📌 Current Status

## Production Readiness

The application is currently suitable for:

* Early users
* Basic production traffic
* Real-world testing
* Architecture evolution

---

# 👨‍💻 Author

Hempreet Singh

Java Full Stack Developer
Backend Engineering Enthusiast

---

# 📄 License

This project is intended for educational and personal development purposes.


[//]: # (# If using docker compose:
[//]: # (
[//]: # (
[//]: # (docker compose down --rmi all
[//]: # (
[//]: # (./gradlew clean build
[//]: # (
[//]: # (docker compose up --build
[//]: # (
[//]: # (
[//]: # (# If using normal docker
[//]: # (
[//]: # (./gradlew clean build
[//]: # (
[//]: # (docker build -t nitnem-tracker .
[//]: # (
[//]: # (docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev nitnem-tracker
[//]: # (OR
[//]: # (docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev -e NITNEM_BOT_TOKEN=YOUR_TOKEN -e DB_URL='jdbc:postgresql://YOUR_DB_URL' -e DB_USERNAME=YOUR_USERNAME -e DB_PASSWORD=YOUR_PASSWORD nitnem-tracker)