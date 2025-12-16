# NYU Activity Center (Spring Boot + Thymeleaf)

## Please refer to the proposal document for the project's features and the advanced topics used. For the project structure and running instructions, please see the README. A short demonstration video is also available on Brightspace.
Campus events and social platform with event creation/enrollment, posts, private chat, and admin panel. Built with Spring Boot + Thymeleaf SSR, persisted with H2 (dev) or MySQL (prod).

## Tech Stack & Highlights
- **Spring Boot / MVC / Thymeleaf**: server-side rendering, classic MVC.
- **Maven**: dependency/build management, profiles (dev: H2 file DB; prod: MySQL).
- **JPA / Hibernate + HikariCP**: ORM, transactions, cache control, JOIN FETCH optimizations.
- **Security / Network**: JWT auth, role/permission checks (Admin/User; canCreate/canEnroll/canComment), custom interceptors.
- **WebSocket**: real-time private messaging.
- **Multithreading**: Tomcat thread pool; transactional consistency for enroll/like/collect flows.
- **Testing**: JUnit5 + Spring Boot Test + MockMvc smoke tests.
- **Other**: Apache POI export, DTO pattern, global exception wrapping.

## Feature Overview
- Events: create (capacity or form-based enrollment), enroll, rate, export enrollment list (Excel), edit (author only), support Markdown in event description, and search events.
- Posts: publish, like, collect, comment, and filter by related `eventId`.
- Profile: edit info/avatar; view "My Events / My Posts / Favorite Events / Favorite Posts".
- Messaging: click avatar to DM, WebSocket chat.
- Admin: edit permissions, manage/delete events and posts.

## Project Structure

```
25fall-java/
├── backend/                          # Spring Boot application root
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── org/example/backend/
│   │   │   │       ├── api/          # JPA Repositories (data access layer)
│   │   │   │       │   ├── EventRepository.java
│   │   │   │       │   ├── PostRepository.java
│   │   │   │       │   ├── UserRepository.java
│   │   │   │       │   └── ...
│   │   │   │       ├── app/          # REST Controllers (API endpoints)
│   │   │   │       │   ├── EventApp.java          # Event CRUD, enrollment, rating
│   │   │   │       │   ├── PostApp.java           # Post CRUD, like, collect
│   │   │   │       │   ├── ProfileApp.java        # User profile management
│   │   │   │       │   ├── AdminApp.java          # Admin operations
│   │   │   │       │   ├── MessageApp.java        # Private messaging
│   │   │   │       │   ├── SearchApp.java         # Event/post search
│   │   │   │       │   └── ViewController.java    # Thymeleaf page routing
│   │   │   │       ├── domain/        # JPA Entities (database models)
│   │   │   │       │   ├── Event.java
│   │   │   │       │   ├── Post.java
│   │   │   │       │   ├── User.java
│   │   │   │       │   ├── AbstractEnrollment.java
│   │   │   │       │   ├── CountEnrollment.java
│   │   │   │       │   ├── FormEnrollment.java
│   │   │   │       │   └── ...
│   │   │   │       ├── dto/           # Data Transfer Objects (API responses)
│   │   │   │       │   ├── EventDto.java
│   │   │   │       │   ├── PostDto.java
│   │   │   │       │   ├── EventBriefDto.java
│   │   │   │       │   └── ...
│   │   │   │       ├── service/       # Business logic layer
│   │   │   │       │   ├── EventService.java
│   │   │   │       │   ├── PostService.java
│   │   │   │       │   ├── AbstractUserService.java
│   │   │   │       │   └── ...
│   │   │   │       ├── config/        # Configuration classes
│   │   │   │       │   ├── SecurityConfig.java    # JWT authentication
│   │   │   │       │   ├── DataSeeder.java        # Initial data seeding
│   │   │   │       │   ├── MyExceptionHandler.java
│   │   │   │       │   └── ...
│   │   │   │       ├── interceptor/  # Request interceptors
│   │   │   │       │   ├── UserInterceptor.java
│   │   │   │       │   └── AdminInterceptor.java
│   │   │   │       ├── util/         # Utility classes
│   │   │   │       │   ├── JwtUtil.java
│   │   │   │       │   ├── ExcelParserUtil.java
│   │   │   │       │   └── ...
│   │   │   │       ├── websocket/    # WebSocket for real-time chat
│   │   │   │       │   ├── WebSocketConfig.java
│   │   │   │       │   └── WebSocketServer.java
│   │   │   │       └── BackendApplication.java    # Main entry point
│   │   │   └── resources/
│   │   │       ├── templates/        # Thymeleaf HTML templates (frontend views)
│   │   │       │   ├── main.html              # Event listing page
│   │   │       │   ├── event.html              # Event detail page
│   │   │       │   ├── createEvent.html        # Event creation form
│   │   │       │   ├── editEvent.html          # Event editing form
│   │   │       │   ├── square.html             # Post listing page
│   │   │       │   ├── postDetail.html       # Post detail page
│   │   │       │   ├── profile.html            # User profile page
│   │   │       │   ├── message.html            # Private chat page
│   │   │       │   ├── admin.html              # Admin panel
│   │   │       │   └── ...
│   │   │       ├── static/            # Static assets (CSS, JS, images)
│   │   │       │   └── js/
│   │   │       │       └── auth.js              # Client-side auth utilities
│   │   │       └── application.yml   # Spring Boot configuration
│   │   │       └── application-dev.yml
│   │   └── test/                     # Test files
│   │       └── java/.../BackendApplicationTests.java
│   ├── pom.xml                       # Maven dependencies and build config
│   ├── h2db/                         # H2 database files (auto-generated)
│   │   └── testdb.mv.db
│   └── images/                       # User-uploaded images
└── README.md                         # This file
```

**Key Components:**
- **`app/`**: REST API controllers handling HTTP requests
- **`domain/`**: JPA entities representing database tables
- **`service/`**: Business logic layer (transactions, data processing)
- **`api/`**: Spring Data JPA repositories for database access
- **`dto/`**: Data transfer objects for API responses
- **`templates/`**: Thymeleaf HTML templates (server-side rendered frontend)
- **`config/`**: Spring configuration (security, exception handling, data seeding)

## Quick Run (Mac / Windows)
### Prereqs
- JDK 17+, Maven 3.6+
- Port 8082 free

### Build (both Mac / Windows)
```bash
cd backend
mvn clean package -DskipTests
```

### Run (dev, H2 file DB)
- Mac/Linux:
```bash
java -jar -Dspring-boot.run.profiles=dev target/backend-0.0.1-SNAPSHOT.jar
```
- Windows (PowerShell):
```powershell
java -Dspring-boot.run.profiles=dev -jar target\backend-0.0.1-SNAPSHOT.jar
```

> **Note for Windows**: The `-D` parameter must come **before** `-jar` in PowerShell. Alternatively, you can use:
> ```powershell
> $env:SPRING_PROFILES_ACTIVE="dev"; java -jar target\backend-0.0.1-SNAPSHOT.jar
> ```

### Access (local)
- Login: `http://localhost:8082/`
- H2 console: `http://localhost:8082/h2-console` (JDBC URL `jdbc:h2:file:./h2db/testdb`, user `sa`)

## Config Notes
- On first run, the app automatically seeds a set of users with different permission levels, including an administrator, staff members who can publish events, and regular users.
- If you want to explore existing data instead of starting from a fresh H2 database, use the deployed instance at [`http://150.136.242.115/`](http://150.136.242.115/).

## Server Deployment / Online Demo

We have deployed the project on a remote server. You can directly use the online demo at:  
[`http://150.136.242.115/`](http://150.136.242.115/)

Below are some test accounts:
- **Super Administrator Account:**  
  - Username: `admin`  
  - Password: `admin`  
  - Permissions: Full administrative privileges (can manage user roles and permissions, delete any event or post, manage system settings)

- **Staff/Admin Accounts (can create/manage events):**  
  - Username: `staff_alice`  
    Password: `staff_alice`  
    Permissions: Can create and manage events, but cannot perform system-level admin actions  
  - Username: `staff_bob`  
    Password: `staff_bob`  
    Permissions: Can create and manage events

- **Regular User Accounts (for signup, enrollment, and comments):**  
  - Username: `participant_chris`  
    Password: `participant_chris`  
    Permissions: Can enroll in events and comment; with admin approval, can be granted event-creation (`canCreate`) permission  
  - Username: `participant_diana`  
    Password: `participant_diana`  
    Permissions: Can only enroll in or comment on events  
  - Username: `participant_eli`  
    Password: `participant_eli`  
    Permissions: Can only enroll in or comment on events

**Notes:**  
- All initial accounts are auto-generated for demonstration and testing purposes if the database is empty on first startup.
- Regular users require admin approval via the admin panel (at `/admin`) to gain event publishing (`canCreate`) privileges.
- Administrators can adjust user permissions at any time via the admin panel.

## Typical User Flows

### As Admin (`admin / admin`)
- Log in via `http://localhost:8082/` (or the server URL).  
- Open the admin panel at `/admin`.  
- Review user list and adjust permissions (e.g., enable `canCreate` for selected users).  
- Manage content:
  - View all events and posts.
  - Delete inappropriate events/posts if necessary.

### As Staff (Event Creator) (`staff_alice`, `staff_bob`)
- Log in and go to `/main` to browse existing events.  
- Create a new event via the “Create Event” entry:
  - Choose enrollment type: **count** (capacity limit) or **form** (custom fields).
  - Fill in times, poster image, introduction, and Markdown description.
  - Submit and verify the event appears on the main page and in **Manage Events**.  
- As the event author:
  - Open the event detail page (`/event?id=...`) and use **Edit Event** to update title/times/description or poster (enrollment type/capacity/form structure and `eventId` are not editable).
  - Export the enrollment list to Excel via the “Export Enrollment List” button when needed.

### As Regular Participant (`participant_chris`, `participant_diana`, `participant_eli`)
- Log in and browse events on `/main`.  
- Click an event to open the detail page; then:
  - Enroll:
    - For **count** events: click “I want to participate”; enrollment will fail with a clear message if capacity is full.
    - For **form** events: fill out the custom form fields and submit.
  - Interact:
    - Use the bottom bar to **collect** (favorite) the event and **rate** it with stars.
    - Click “Posts” or go to `/square` to view and create posts related to events (if you have comment permission).
    - On the event detail page, view related posts; on the post detail page, click the author avatar/name to start a private chat.  
- Open `/profile` to:
  - See **My Events** (created by you, if you have `canCreate`), **My Posts**, and your **Favorite Events / Favorite Posts** tabs.

### Using Private Chat
- Open the **Messages** page from the navigation bar (link to `/message`) to see your existing conversations.  
- Start a new chat in two ways:
  - From a **post detail** page: click the author’s avatar or name to open a direct conversation with that user.  
  - From other entry points that show usernames (e.g., profile links) where the UI routes you to `/message?userId=...`.  
- In the **Messages** page:
  - Select a user/conversation from the list on the left.
  - Type your message in the input box at the bottom and send; messages are delivered in real time via WebSocket.  
  - You can navigate away and come back to see the conversation history with that user.

## Tests
```bash
mvn test
```
Coverage: main page reachable, admin login (JWT), signup/login with hashed passwords.

> Note: you should run tests on your local development machine (not on the remote server).


## Data & Static Assets
- H2 data file: `backend/h2db/testdb.mv.db` (file-based, persistent).
- Images: `backend/images` (uploads); static assets in `backend/src/main/resources/static/`.

