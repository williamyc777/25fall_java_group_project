# CS9053 Java Final Project Proposal

**Project Title:** NYU Activity Center - Campus Events and Entertainment Platform

**Student Names:** Cheng Yang (cy2932), Yixin Leng (yl13851), Zuqing Gao (zg2921)

**Date:** December 14, 2024

---

## 1. Project Overview

The NYU Activity Center is a centralized web-based system designed to help university communities organize, discover, and participate in campus events and activities. The system serves as a digital hub to enhance student engagement, simplify event management, and strengthen social connections among students, faculty, and staff.

### Target Users

- **Event Participants:** Students or staff who join and attend campus events
- **Event Organizers:** Clubs, associations, or faculty members who create and manage events
- **Administrators:** System operators who review and manage content, user permissions, and system-wide operations
- **Additional Users:** Visitors outside the university who can view or join public events

---

## 2. Overall Goals

The main goal is to create a centralized, user-friendly, and intelligent campus event system that:

- Enables effortless event creation, publication, and management with flexible enrollment options (capacity-based or form-based)
- Allows users to explore and register for events based on personal interests and preferences
- Encourages campus socialization through comments, reviews, ratings, and real-time chat features
- Enhances community spirit and promotes a vibrant, participatory campus culture
- Provides administrators with comprehensive tools for content and user management

---

## 3. Key Functionalities

### 3.1 Event Organization
Users with appropriate permissions can easily create, edit, and publish events. The system supports:
- **Capacity-based enrollment:** Events with a fixed participant limit
- **Form-based enrollment:** Events requiring custom form submissions
- **Event details:** Title, description, dates, poster images, and markdown content
- **Event management:** View, edit, and export enrollment lists for created events

### 3.2 Event Discovery
Users can discover events through:
- **Browse all events:** View all available events on the main page
- **Search functionality:** Search events by keywords, titles, or content
- **Event filtering:** Filter events by various criteria
- **Event details:** View comprehensive information including enrollment status, participant count, and ratings

### 3.3 User Interaction
The system facilitates social interaction through:
- **Posts:** Create posts related to events or general topics
- **Comments:** Comment on posts and events
- **Likes and Collections:** Like posts and collect favorite events
- **Real-time Chat:** Private messaging between users via WebSocket
- **User Profiles:** View and edit personal profiles with avatar support

### 3.4 Event Reviews and Ratings
Users can:
- **Rate events:** Provide star ratings (1-5 stars) for attended events
- **Comment on events:** Share experiences and thoughts about events
- **View ratings:** See average ratings and individual reviews

### 3.5 Social Sharing
- **Event links:** Share event URLs with others
- **Post sharing:** Share posts and event-related content
- **User mentions:** Reference other users in comments and posts

### 3.6 Authentication and Security
- **JWT Authentication:** Secure login using JSON Web Tokens
- **User Registration:** Self-service account creation
- **Permission Management:** Role-based access control (Admin, User with various permissions)
- **Session Management:** Token-based authentication with cookie support

### 3.7 Real-Time Communication
- **WebSocket Integration:** Live chat functionality for private messaging
- **Message Notifications:** Real-time updates for new messages
- **Chat History:** Persistent message storage and retrieval

### 3.8 Administrative Features
- **User Management:** View all users, edit permissions (canCreate, canEnroll, canComment)
- **Event Management:** View all events, delete inappropriate events
- **Post Management:** View all posts, delete inappropriate content
- **Data Export:** Export event enrollment lists to Excel format

---

## 4. Technical Design

### 4.1 Frontend
- **Template Engine:** Thymeleaf 3.1.2 (Server-Side Rendering)
- **UI Framework:** Bootstrap 5.3.0
- **Icons:** Bootstrap Icons 1.11.0
- **Client-side Scripting:** Vanilla JavaScript (ES6+)
- **Architecture:** Traditional MVC (Model-View-Controller)

### 4.2 Backend
- **Framework:** Spring Boot 3.2.4
- **Web Framework:** Spring Web MVC
- **Data Access:** Spring Data JPA
- **Template Engine:** Thymeleaf Spring 6
- **Build Tool:** Maven 3.6+

### 4.3 Database
- **Development:** H2 Database (file-based, persistent)
- **Production:** MySQL 8.3.0 (configured)
- **ORM:** JPA/Hibernate 6.4.4
- **Connection Pool:** HikariCP

### 4.4 Additional Technologies
- **Authentication:** JWT (JSON Web Tokens) via Auth0 Java JWT
- **Real-time Communication:** Spring WebSocket
- **Excel Export:** Apache POI 5.2.5
- **JSON Processing:** Jackson
- **Java Version:** JDK 17+

---

## 5. Advanced Java Concepts Applied

This project integrates multiple advanced Java concepts with the Final Project requirements:

### 5.1 Spring Framework (Boot, MVC, Data JPA)
- **RESTful Web Services:** Implementation of REST APIs for all backend operations
- **Dependency Injection:** Extensive use of Spring's IoC container for loose coupling
- **AOP (Aspect-Oriented Programming):** Transaction management and cross-cutting concerns
- **Data Access Layer:** JPA repositories for database operations with custom queries
- **Security:** JWT-based authentication and authorization

### 5.2 Multithreading and Concurrency
- **Thread Pool Management:** Spring Boot's embedded Tomcat handles multiple HTTP requests concurrently
- **Database Transactions:** JPA transaction management ensures data consistency under concurrent access
- **WebSocket Sessions:** Multiple concurrent WebSocket connections for real-time chat
- **Synchronization:** Proper handling of concurrent event enrollments and data updates

### 5.3 Database Integration with JPA
- **Entity Management:** Complex entity relationships (User, Event, Post, Comment, Enrollment)
- **Lazy Loading:** Efficient data fetching with lazy initialization and eager loading strategies
- **Transaction Management:** `@Transactional` annotations ensure ACID properties
- **Query Optimization:** JPQL queries with JOIN FETCH for performance
- **Cache Management:** EntityManager cache control for data consistency

### 5.4 Network Communication via WebSocket
- **Real-time Bidirectional Communication:** WebSocket implementation for live chat
- **Session Management:** WebSocket session handling and message routing
- **Event-driven Architecture:** WebSocket event handlers for connection lifecycle

### 5.5 Maven Project Management
- **Build Automation:** Maven lifecycle management (compile, test, package, install)
- **Dependency Management:** Centralized dependency resolution via `pom.xml`
- **Modular Structure:** Well-organized project structure with clear separation of concerns
- **Profile Management:** Environment-specific configurations (dev, production)

### 5.6 Additional Advanced Features
- **Exception Handling:** Custom exception handling with global response advice
- **DTO Pattern:** Data Transfer Objects for API responses
- **Service Layer Pattern:** Business logic separation from controllers
- **Repository Pattern:** Data access abstraction
- **Factory Pattern:** Used in enrollment type handling (CountEnrollment, FormEnrollment)

---

## 6. Expected Outcomes

The completed system will:

- **Support Concurrent Users:** Handle multiple simultaneous requests through Spring Boot's multi-threaded architecture
- **Real-time Interaction:** Enable live chat and instant updates via WebSocket
- **Persistent Data Storage:** Maintain data integrity with JPA and transactional management
- **Demonstrate Java Strengths:** Showcase Java's capabilities in:
  - Backend web application development
  - Multithreading and concurrency control
  - Database integration and ORM
  - Network programming (WebSocket)
  - Enterprise application architecture
- **Production-Ready Features:** Include authentication, authorization, error handling, and data validation
- **Scalable Architecture:** Support future enhancements and feature additions

---

## 7. Timeline

### Week 1-2: Backend API Design and Database Setup
- Design database schema and entity relationships
- Implement JPA repositories and services
- Create REST API endpoints
- Set up authentication and authorization
- Configure development environment (H2 database)

### Week 3: Frontend Integration and Authentication System
- Migrate from Vue.js to Thymeleaf (as per professor feedback)
- Implement server-side rendering with Thymeleaf templates
- Integrate Bootstrap UI framework
- Complete authentication flow (login, registration, JWT)
- Implement user profile management

### Week 4: Core Features and Real-time Communication
- Event creation and management
- Post creation and interaction (like, collect, comment)
- Event enrollment system (capacity-based and form-based)
- WebSocket chat implementation
- Admin panel development

### Week 5: Advanced Features and Finalization
- Excel export functionality
- Search and filtering
- Rating and review system
- Performance optimization
- Testing and bug fixes
- Documentation (README, setup guides)
- Final presentation and submission

---

## 8. Project Structure

```
25fall-java/
├── backend/                    # Spring Boot application
│   ├── src/main/java/         # Java source code
│   │   ├── app/              # REST controllers
│   │   ├── config/           # Configuration classes
│   │   ├── domain/           # Entity classes
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── service/          # Business logic layer
│   │   ├── api/              # JPA repositories
│   │   ├── util/             # Utility classes
│   │   └── websocket/        # WebSocket handlers
│   ├── src/main/resources/
│   │   ├── templates/        # Thymeleaf HTML templates
│   │   ├── static/           # Static resources (CSS, JS, images)
│   │   └── application*.yml  # Configuration files
│   └── pom.xml               # Maven dependencies
└── frontend/                  # Legacy Vue.js code (not used)
```

---

## 9. Key Technical Challenges and Solutions

### 9.1 Server-Side Rendering Migration
**Challenge:** Migrating from Vue.js SPA to Thymeleaf SSR
**Solution:** Implemented Thymeleaf templates with server-side data binding and client-side JavaScript for interactivity

### 9.2 Lazy Loading and Transaction Management
**Challenge:** `LazyInitializationException` when accessing lazy-loaded collections
**Solution:** Used `@Transactional` annotations, JPQL JOIN FETCH queries, and explicit entity initialization

### 9.3 Data Consistency
**Challenge:** Ensuring data consistency with concurrent operations
**Solution:** Implemented proper transaction boundaries, EntityManager cache management, and optimistic locking

### 9.4 Real-time Communication
**Challenge:** Implementing bidirectional WebSocket communication
**Solution:** Spring WebSocket configuration with custom message handlers and session management

---

## 10. Future Enhancements

Potential improvements for future iterations:

- **Mobile App:** Native mobile applications for iOS and Android
- **Advanced Search:** Full-text search with Elasticsearch
- **Recommendation Engine:** Machine learning-based event recommendations
- **Analytics Dashboard:** Event statistics and user engagement metrics
- **Email Notifications:** Automated email reminders for events
- **Calendar Integration:** Sync events with Google Calendar, iCal
- **Social Media Integration:** Direct sharing to social platforms
- **Multi-language Support:** Internationalization (i18n)

---

**Last Updated:** December 14, 2024

