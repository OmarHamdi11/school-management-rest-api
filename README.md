# School Management REST API

A comprehensive RESTful API for managing educational institutions, built with Spring Boot. This system handles courses, instructors, students, and enrollment management with JWT-based authentication and role-based access control.

## 🎯 Overview

This application provides a complete backend solution for school management, featuring separate interfaces for instructors and students with secure authentication, course management, and enrollment tracking.

## ✨ Key Features

### 🔐 Authentication & Security
- JWT (JSON Web Token) based authentication
- Role-based access control (INSTRUCTOR, STUDENT)
- BCrypt password encryption
- Stateless session management
- Protected endpoints with method-level security

### 👨‍🏫 Instructor Features
- Create, update, and delete courses
- Manage course details (name, description, price, duration, level)
- View all courses created by instructor
- Track student enrollments per course
- Full and partial course updates (PUT/PATCH)

### 👨‍🎓 Student Features
- Browse available courses with pagination
- Enroll in courses
- Unenroll from courses
- View personal course enrollments
- Check enrollment status for specific courses

### 📚 Public Features
- View all courses (paginated, sorted, filtered)
- View individual course details
- No authentication required for browsing

### 🛠️ Technical Features
- RESTful API design with proper HTTP methods
- Pagination and sorting support
- Input validation with custom error messages
- Global exception handling
- Standardized API response format
- Single Table Inheritance for User entities
- Many-to-Many relationship management

## 🏗️ Architecture

### Technology Stack

- **Framework**: Spring Boot 3.5.6
- **Java Version**: Java 21
- **Security**: Spring Security with JWT
- **ORM**: Spring Data JPA with Hibernate
- **Database**: MySQL
- **Build Tool**: Maven
- **Additional Libraries**:
    - Lombok - Reduce boilerplate code
    - ModelMapper - Object mapping
    - JJWT - JWT token generation and validation
    - SpringDoc OpenAPI - API documentation

### Design Patterns

- **Layered Architecture**: Controller → Service → Repository
- **Dependency Injection**: Constructor-based injection
- **DTO Pattern**: Separate request/response objects
- **Single Table Inheritance**: User entity hierarchy
- **Repository Pattern**: Spring Data JPA repositories

## 📊 Database Schema

### Entity Relationships

```
User (Abstract)
├── Instructor (specialization)
│   └── Courses (One-to-Many)
└── Student (major)
    └── EnrolledCourses (Many-to-Many)

Course
├── Instructor (Many-to-One)
└── Students (Many-to-Many)
```

### Tables

- **user**: Single table for both instructors and students (user_type discriminator)
- **course**: Course information with instructor reference
- **student_course**: Join table for enrollments

## 🚀 Getting Started

### Prerequisites

- **JDK 21** or higher
- **Maven 3.6+**
- **MySQL 8.0+**

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd school-management-rest-api
```

2. **Configure the database**

Create a MySQL database:
```sql
CREATE DATABASE school;
```

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/school
spring.datasource.username=your_username
spring.datasource.password=your_password

app.jwt-secret=your_secret_key_here
app-jwt-expiration-millisecond=86400000
```

3. **Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## 📌 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Login and get JWT token | Public |

### Course Endpoints - Public

| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/api/courses` | Get all courses (paginated) | pageNo, pageSize, sortBy, sortDir |
| GET | `/api/courses/{id}` | Get course by ID | - |

### Course Endpoints - Instructor

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/courses` | Create new course | INSTRUCTOR |
| PUT | `/api/courses/{id}` | Update course (full) | INSTRUCTOR |
| PATCH | `/api/courses/{id}` | Update course (partial) | INSTRUCTOR |
| DELETE | `/api/courses/{id}` | Delete course | INSTRUCTOR |
| GET | `/api/courses/my-courses` | Get instructor's courses | INSTRUCTOR |

### Course Endpoints - Student

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/courses/{id}/enroll` | Enroll in course | STUDENT |
| DELETE | `/api/courses/{id}/unenroll` | Unenroll from course | STUDENT |
| GET | `/api/courses/my-enrollments` | Get enrolled courses | STUDENT |
| GET | `/api/courses/{id}/check-enrollment` | Check enrollment status | STUDENT |

## 📝 Request/Response Examples

### Register User

**Request:**
```json
POST /api/auth/register
{
  "username": "john_instructor",
  "password": "password123",
  "role": "INSTRUCTOR",
  "specialization": "Computer Science"
}
```

**Response:**
```json
"User registered successfully"
```

### Login

**Request:**
```json
POST /api/auth/login
{
  "username": "john_instructor",
  "password": "password123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "username": "john_instructor",
  "role": "INSTRUCTOR"
}
```

### Create Course (Instructor)

**Request:**
```json
POST /api/courses
Authorization: Bearer <token>

{
  "name": "Introduction to Spring Boot",
  "description": "Learn Spring Boot from scratch",
  "price": 299.99,
  "duration": 40,
  "level": "BEGINNER"
}
```

**Response:**
```json
{
  "success": true,
  "status": 201,
  "message": "Course Created Successfully",
  "timestamp": "2025-11-02T10:30:00",
  "data": {
    "id": 1,
    "name": "Introduction to Spring Boot",
    "description": "Learn Spring Boot from scratch",
    "price": 299.99,
    "duration": 40,
    "level": "BEGINNER",
    "instructorId": 1,
    "instructorName": "john_instructor",
    "enrolledStudentsCount": 0
  }
}
```

### Get All Courses (Paginated)

**Request:**
```
GET /api/courses?pageNo=0&pageSize=10&sortBy=name&sortDir=asc
```

**Response:**
```json
{
  "success": true,
  "status": 200,
  "message": "Courses retrieved successfully",
  "timestamp": "2025-11-02T10:30:00",
  "data": {
    "pageNo": 0,
    "pageSize": 10,
    "totalElements": 25,
    "totalPages": 3,
    "last": false,
    "first": true,
    "pageContent": [
      {
        "id": 1,
        "name": "Introduction to Spring Boot",
        "description": "Learn Spring Boot from scratch",
        "price": 299.99,
        "duration": 40,
        "level": "BEGINNER",
        "instructorId": 1,
        "instructorName": "john_instructor",
        "enrolledStudentsCount": 15
      }
    ]
  }
}
```

### Enroll in Course (Student)

**Request:**
```json
POST /api/courses/1/enroll
Authorization: Bearer <student_token>
```

**Response:**
```json
{
  "success": true,
  "status": 200,
  "message": "Successfully enrolled in the course",
  "timestamp": "2025-11-02T10:30:00",
  "data": null
}
```

### Error Response Example

**Response:**
```json
{
  "success": false,
  "status": 404,
  "message": "Course not found with Id: '999'",
  "timestamp": "2025-11-02T10:30:00",
  "path": "/api/courses/999"
}
```

## 🔒 Security Implementation

### JWT Authentication Flow

1. User registers/logs in with credentials
2. Server validates and generates JWT token
3. Client includes token in Authorization header: `Bearer <token>`
4. Server validates token for each request
5. Token expires after 24 hours (configurable)

### Password Security

- Passwords hashed using BCrypt
- Plain text passwords never stored
- Password strength validation on registration

### Authorization

- Method-level security with `@PreAuthorize`
- Role-based access control (RBAC)
- Instructors can only modify their own courses
- Students can only manage their own enrollments

## 🎨 Key Implementation Details

### Single Table Inheritance

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
public abstract class User {
    // Base user fields
}

@Entity
@DiscriminatorValue("INSTRUCTOR")
public class Instructor extends User {
    private String specialization;
    // Instructor-specific fields
}

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {
    private String major;
    // Student-specific fields
}
```

### Bidirectional Relationship Management

```java
// Helper methods for maintaining relationship consistency
public void enrollInCourse(Course course) {
    this.enrolledCourses.add(course);
    course.getEnrolledStudents().add(this);
}

public void unenrollFromCourse(Course course) {
    this.enrolledCourses.remove(course);
    course.getEnrolledStudents().remove(this);
}
```

### Input Validation

```java
@NotBlank(message = "Course name is required")
@Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
private String name;

@Pattern(regexp = "BEGINNER|INTERMEDIATE|ADVANCED",
        message = "Level must be BEGINNER, INTERMEDIATE, or ADVANCED")
private String level;
```

## 📦 Project Structure

```
school-management-rest-api/
├── src/main/java/com/springboot/school_management/
│   ├── SchoolManagementRestApiApplication.java
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   └── CourseController.java
│   ├── entity/
│   │   ├── User.java (Abstract)
│   │   ├── Instructor.java
│   │   ├── Student.java
│   │   ├── Course.java
│   │   └── Role.java (Enum)
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   └── ResourceNotFoundException.java
│   ├── payload/
│   │   ├── CourseDto.java
│   │   ├── CourseRequest.java
│   │   ├── JwtAuthResponse.java
│   │   ├── LoginDto.java
│   │   └── RegisterDto.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── InstructorRepository.java
│   │   ├── StudentRepository.java
│   │   └── CourseRepository.java
│   ├── response/
│   │   ├── ApiResponse.java
│   │   └── PageResponse.java
│   ├── security/
│   │   ├── JwtAuthenticationEntryPoint.java
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── JwtTokenProvider.java
│   │   └── CustomUserDetailsService.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── AuthServiceImpl.java
│   │   ├── CourseService.java
│   │   └── CourseServiceImpl.java
│   └── utils/
│       ├── AppConstants.java
│       └── SecurityUtils.java
└── pom.xml
```

## 🧪 Testing

Run tests:
```bash
mvn test
```

## 🔧 Configuration

### Application Properties

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/school
spring.datasource.username=root
spring.datasource.password=your_password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
logging.level.org.hibernate.SQL=trace

# JWT Configuration
app.jwt-secret=your_secret_key
app-jwt-expiration-millisecond=86400000
```

## 📈 Future Enhancements

- [ ] Admin role and dashboard
- [ ] Course categories and tags
- [ ] Advanced search and filtering
- [ ] Course ratings and reviews
- [ ] Assignment and grade management
- [ ] Email notifications
- [ ] File upload for course materials
- [ ] Payment integration
- [ ] Certificate generation
- [ ] Real-time notifications (WebSocket)
- [ ] Analytics and reporting
- [ ] Multi-language support
- [ ] Docker containerization
- [ ] CI/CD pipeline
- [ ] Comprehensive unit and integration tests

## 🐛 Common Issues & Solutions

**Issue: JWT Token Expired**
```
Solution: Login again to get a new token
Token validity: 24 hours
```

**Issue: Access Denied**
```
Solution: Ensure you're using the correct role token
Instructors cannot access student endpoints and vice versa
```

**Issue: Course Not Found After Creation**
```
Solution: Check if the instructor ID matches the token user
Only course owner can modify/delete courses
```

## 📚 API Standards

- RESTful conventions followed
- HTTP status codes properly used (200, 201, 404, 403, etc.)
- Consistent response structure
- Proper use of HTTP methods (GET, POST, PUT, PATCH, DELETE)
- Query parameters for pagination and sorting

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## 📄 License

This project is open source and available under the MIT License.

## 👨‍💻 Developer

**Omar Hamdi Ellafy**

---

**Built with ❤️ using Spring Boot, Spring Security, JWT, and MySQL**