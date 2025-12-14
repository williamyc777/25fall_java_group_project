# NYU Activity Center

A full-stack web application for managing events, posts, and user interactions at NYU. This project uses Spring Boot with Thymeleaf for server-side rendering, providing a traditional MVC architecture. The frontend is built with Thymeleaf templates, Bootstrap 5, and vanilla JavaScript.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Default Accounts](#default-accounts)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

## Features

- **User Management**: Registration, login, profile management
- **Event Management**: Create, view, enroll in events with capacity limits or form-based enrollment
- **Post Management**: Create posts, like, collect, and comment
- **Admin Panel**: User permission management, event/post deletion
- **Private Messaging**: Real-time chat between users
- **Excel Export**: Export event enrollment lists

## Prerequisites

Before running this project, ensure you have the following installed:

- **Java**: JDK 17 or higher
  - Check version: `java -version`
  - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
  
- **Maven**: 3.6+ (for building and running the project)
  - Check version: `mvn -version`
  - Download: [Apache Maven](https://maven.apache.org/download.cgi)
  
- **Git**: (for cloning the repository)
  - Check version: `git --version`
  - Download: [Git](https://git-scm.com/downloads)

## Installation

1. **Clone the repository**
   ```bash
   git clone <your-repository-url>
   cd 25fall-java
   ```

2. **Navigate to the backend directory**
   ```bash
   cd backend
   ```

3. **Build the project** (optional, Maven will build automatically on first run)
   ```bash
   mvn clean install
   ```

## Running the Application

### Quick Start (Recommended)

The easiest way to run the application is using the provided scripts:

1. **Start the server**
   ```bash
   cd /path/to/25fall-java
   ./start-server.sh
   ```

2. **Or restart the server** (stops existing instance and starts a new one)
   ```bash
   ./restart-server.sh
   ```

3. **Stop the server**
   ```bash
   ./stop-server.sh
   ```

### Manual Start

Alternatively, you can start the server manually:

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Access the Application

Once the server starts (takes about 15-20 seconds), open your browser and navigate to:

```
http://localhost:8082/
```

You should see the login page.

## Default Accounts

The application automatically seeds the database with test accounts on first startup:

### Admin Account
- **Username**: `admin`
- **Password**: `admin`
- **Permissions**: Full access to admin panel

### Regular User Accounts
- **Username**: `user1`, `user2`, `user3`, etc.
- **Password**: Same as username (e.g., `user1` / `user1`)
- **Permissions**: Varies by user (some can create events, some can only enroll)

**Note**: User `user1` typically has `canCreate` permission enabled.

## Project Structure

```
25fall-java/
├── backend/                 # Spring Boot backend application
│   ├── src/
│   │   └── main/
│   │       ├── java/        # Java source code (Controllers, Services, Domain)
│   │       ├── resources/
│   │       │   ├── templates/  # Thymeleaf HTML templates (Frontend views)
│   │       │   ├── static/     # Static resources (CSS, JS, images)
│   │       │   │   └── js/     # Client-side JavaScript utilities
│   │       │   └── application*.yml  # Configuration files
│   │       └── ...
│   ├── pom.xml             # Maven dependencies
│   └── h2db/               # H2 database files (auto-generated, gitignored)
├── frontend/               # Vue.js frontend (legacy, not used - migrated to Thymeleaf)
├── restart-server.sh       # Server restart script
├── start-server.sh         # Server start script
└── stop-server.sh          # Server stop script
```

**Note**: The `frontend/` directory contains legacy Vue.js code from an earlier version. The current implementation uses **Thymeleaf templates** located in `backend/src/main/resources/templates/` for server-side rendering. The frontend is fully integrated into the Spring Boot application.

## Configuration

### Database Configuration

The application uses **H2 file-based database** in development mode (default). The database file is stored in `backend/h2db/testdb.mv.db`.

**Development Profile** (`application-dev.yml`):
- Database: H2 (file-based, persistent)
- Location: `./h2db/testdb`
- Auto-creates tables on first run
- Data persists between restarts

**Production Profile** (`application.yml`):
- Database: MySQL (configured but not used by default)
- Requires MySQL server setup

### Changing Database

To use MySQL instead of H2:

1. Install and start MySQL server
2. Create a database named `cs304_database`
3. Update `backend/src/main/resources/application.yml` with your MySQL credentials
4. Run without the `-Dspring-boot.run.profiles=dev` flag

### Port Configuration

The server runs on port **8082** by default. To change it:

1. Edit `backend/src/main/resources/application-dev.yml`
2. Change `server.port: 8082` to your desired port
3. Restart the server

## Troubleshooting

### Server Won't Start

**Port already in use:**
```bash
# Check what's using port 8082
lsof -i :8082

# Kill the process (replace PID with actual process ID)
kill -9 <PID>
```

**Compilation errors:**
```bash
# Clean and rebuild
cd backend
mvn clean compile
```

### Database Issues

**Database locked:**
- Stop the server properly using `./stop-server.sh`
- If locked, delete `backend/h2db/testdb.lock.db` (if exists)

**Reset database:**
- Stop the server
- Delete `backend/h2db/` directory
- Restart server (will create fresh database)

### Browser Issues

**Page not loading:**
- Clear browser cache (Cmd+Shift+R on Mac, Ctrl+Shift+R on Windows)
- Check server logs: `tail -f /tmp/spring-boot.log`
- Verify server is running: `curl http://localhost:8082/`

### Viewing Logs

Server logs are written to `/tmp/spring-boot.log`. To view:
```bash
tail -f /tmp/spring-boot.log
```

## Testing the Application

### Basic Functionality Tests

1. **Login/Registration**
   - Register a new account
   - Login with existing account

2. **Event Management**
   - Create an event (requires `canCreate` permission)
   - View events on main page
   - Enroll in an event
   - Export enrollment list (as event creator)

3. **Post Management**
   - Create a post
   - Like and collect posts
   - Comment on posts

4. **Admin Panel**
   - Login as `admin`
   - Access `/admin` page
   - Edit user permissions
   - Delete events/posts

### Admin Panel Features

Access the admin panel at: `http://localhost:8082/admin`

**User Management:**
- View all users
- Edit user permissions (canCreate, canEnroll, canComment)

**Event Management:**
- View all events
- Delete events

**Post Management:**
- View all posts
- Delete posts

## Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## Technology Stack

- **Backend**: 
  - Spring Boot 3.2.4
  - Spring Data JPA
  - Spring Web MVC
- **Frontend**: 
  - Thymeleaf (Server-Side Rendering)
  - Bootstrap 5.3.0 (UI Framework)
  - Vanilla JavaScript (Client-side scripting)
- **Database**: 
  - H2 (development, file-based)
  - MySQL (production, configured)
- **Build Tool**: Maven 3.6+
- **Java Version**: JDK 17+
- **Additional Libraries**:
  - Apache POI (Excel export)
  - JWT (Authentication)
  - WebSocket (Real-time messaging)


## Support

For issues or questions, please contact the development team or create an issue in the repository.

---

**Last Updated**: December 2025
