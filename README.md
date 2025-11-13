# 🎓 School Management REST API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive RESTful API for managing educational institutions, built with Spring Boot. This system provides complete course management, student enrollment, instructor dashboards, and a review system with JWT-based authentication and role-based access control.

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [API Endpoints](#-api-endpoints)
- [Request/Response Examples](#-requestresponse-examples)
- [Security](#-security)
- [Database Schema](#-database-schema)
- [Project Structure](#-project-structure)
- [Configuration](#-configuration)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### 🔐 Authentication & Security
- JWT (JSON Web Token) based authentication
- Role-based access control (INSTRUCTOR, STUDENT)
- BCrypt password encryption
- Stateless session management
- Protected endpoints with method-level security

### 👨‍🏫 Instructor Features
- **Course Management**
    - Create, update (PUT/PATCH), and delete courses
    - Manage course details (name, description, price, duration, level)
    - View all created courses
    - Track student enrollments per course

- **Dashboard & Analytics**
    - Comprehensive instructor dashboard
    - Statistics (total courses, students, reviews, average rating)
    - Rating distribution analysis
    - Most popular and highest-rated courses
    - Recent reviews and course activity

- **Profile Management**
    - View and update instructor profile
    - Manage specialization

### 👨‍🎓 Student Features
- **Course Enrollment**
    - Browse all available courses with pagination
    - Enroll in courses
    - Unenroll from courses
    - View personal enrollments
    - Check enrollment status

- **Review System**
    - Write course reviews with ratings (1-5)
    - Update and delete own reviews
    - View all personal reviews
    - Check if already reviewed a course

- **Dashboard & Analytics**
    - Student dashboard with enrolled courses
    - Personal statistics (enrollments, reviews, average rating)
    - Rating distribution of given reviews
    - Most enrolled course level
    - Favorite instructor identification

### 📚 Public Features
- View all courses (paginated, sorted, filtered)
- View individual course details
- View all instructors with statistics
- View instructor profiles and courses
- Search instructors by specialization
- View course reviews with pagination
- Get course average ratings and review counts
- No authentication required for browsing

### 🛠️ Technical Features
- RESTful API design with proper HTTP methods
- Pagination and sorting support
- Input validation with custom error messages
- Global exception handling
- Standardized API response format
- Single Table Inheritance for User entities
- Many-to-Many relationship management
- Bidirectional relationship synchronization
- OpenAPI/Swagger documentation

## 🏗️ Technology Stack

### Core Technologies
- **Framework**: Spring Boot 3.5.6
- **Java Version**: Java 21
- **Security**: Spring Security + JWT (JJWT 0.13.0)
- **ORM**: Spring Data JPA with Hibernate
- **Database**: MySQL 8.0+
- **Build Tool**: Maven

### Additional Libraries
| Library | Version | Purpose |
|---------|---------|---------|
| Lombok | 1.18.42 | Reduce boilerplate code |
| ModelMapper | 3.2.5 | Object mapping (Entity ↔ DTO) |
| JJWT | 0.13.0 | JWT token generation and validation |
| SpringDoc OpenAPI | 2.8.13 | API documentation |

### Design Patterns
- **Layered Architecture**: Controller → Service → Repository
- **Dependency Injection**: Constructor-based injection
- **DTO Pattern**: Separate request/response objects
- **Single Table Inheritance**: User entity hierarchy
- **Repository Pattern**: Spring Data JPA repositories
- **Builder Pattern**: Lombok @Builder annotation

## 🎨 Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Client Layer                             │
│              (Web/Mobile/Postman/Swagger)                    │
└────────────────────┬────────────────────────────────────────┘
                     │ HTTP Requests (JSON)
                     ↓
┌─────────────────────────────────────────────────────────────┐
│                  Security Layer                              │
│        JWT Filter → Authentication → Authorization           │
└────────────────────┬────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────────────┐
│                 Controller Layer                             │
│  AuthController | CourseController | InstructorController   │
│      StudentController | ReviewController                    │
└────────────────────┬────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────────────┐
│                  Service Layer                               │
│    AuthService | CourseService | InstructorService          │
│         StudentService | ReviewService                       │
└────────────────────┬────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────────────┐
│                Repository Layer                              │
│    UserRepository | CourseRepository | ReviewRepository     │
│    InstructorRepository | StudentRepository                 │
└────────────────────┬────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────────────┐
│                  Database Layer                              │
│                    MySQL Database                            │
│        (user, course, review, student_course)                │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Getting Started

### Prerequisites

- **JDK 21** or higher
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

### Installation Steps

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/school-management-rest-api.git
cd school-management-rest-api
```

2. **Create MySQL Database**
```sql
CREATE DATABASE school CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **Configure Database Connection**

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/school
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
app.jwt-secret=your_secret_key_here_min_256_bits
app-jwt-expiration-millisecond=86400000
```

4. **Build the Project**
```bash
mvn clean install
```

5. **Run the Application**
```bash
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080`

6. **Access API Documentation**

Swagger UI: `http://localhost:8080/swagger-ui.html`

## 📌 API Endpoints

### 🔑 Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Login and get JWT token | Public |

### 📚 Course Endpoints

#### Public Endpoints
| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/api/courses` | Get all courses | `pageNo`, `pageSize`, `sortBy`, `sortDir` |
| GET | `/api/courses/{id}` | Get course by ID | - |

#### Instructor Only
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/courses` | Create new course | INSTRUCTOR |
| PUT | `/api/courses/{id}` | Update course (full) | INSTRUCTOR |
| PATCH | `/api/courses/{id}` | Update course (partial) | INSTRUCTOR |
| DELETE | `/api/courses/{id}` | Delete course | INSTRUCTOR |
| GET | `/api/courses/my-courses` | Get instructor's courses | INSTRUCTOR |

#### Student Only
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/courses/{id}/enroll` | Enroll in course | STUDENT |
| DELETE | `/api/courses/{id}/unenroll` | Unenroll from course | STUDENT |
| GET | `/api/courses/my-enrollments` | Get enrolled courses | STUDENT |
| GET | `/api/courses/{id}/check-enrollment` | Check enrollment status | STUDENT |

### 👨‍🏫 Instructor Endpoints

#### Public Endpoints
| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/api/instructors` | Get all instructors | `pageNo`, `pageSize`, `sortBy`, `sortDir` |
| GET | `/api/instructors/{id}` | Get instructor by ID | - |
| GET | `/api/instructors/{id}/courses` | Get instructor's courses | - |
| GET | `/api/instructors/search` | Search by specialization | `specialization` |

#### Instructor Only
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/instructors/profile` | Get own profile | INSTRUCTOR |
| PUT | `/api/instructors/profile` | Update profile | INSTRUCTOR |
| GET | `/api/instructors/dashboard` | Get dashboard data | INSTRUCTOR |
| GET | `/api/instructors/statistics` | Get statistics | INSTRUCTOR |

### 👨‍🎓 Student Endpoints

#### Public Endpoints
| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/api/students` | Get all students | `pageNo`, `pageSize`, `sortBy`, `sortDir` |
| GET | `/api/students/{id}` | Get student by ID | - |

#### Student Only
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/students/profile` | Get own profile | STUDENT |
| PUT | `/api/students/profile` | Update profile | STUDENT |
| GET | `/api/students/dashboard` | Get dashboard data | STUDENT |
| GET | `/api/students/statistics` | Get statistics | STUDENT |

### ⭐ Review Endpoints

#### Public Endpoints
| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/api/reviews/{id}` | Get review by ID | - |
| GET | `/api/reviews/course/{courseId}` | Get course reviews | `pageNo`, `pageSize`, `sortBy`, `sortDir` |
| GET | `/api/reviews/course/{courseId}/average` | Get average rating | - |
| GET | `/api/reviews/course/{courseId}/count` | Get reviews count | - |

#### Student Only
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/reviews` | Create review | STUDENT |
| PUT | `/api/reviews/{id}` | Update review | STUDENT |
| DELETE | `/api/reviews/{id}` | Delete review | STUDENT |
| GET | `/api/reviews/my-reviews` | Get all my reviews | STUDENT |
| GET | `/api/reviews/my-reviews/course/{courseId}` | Get my review for course | STUDENT |
| GET | `/api/reviews/check/course/{courseId}` | Check if reviewed | STUDENT |

## 📝 Request/Response Examples

### Register Instructor

**Request:**
```json
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePass123!",
  "role": "INSTRUCTOR",
  "specialization": "Computer Science"
}
```

**Response:**
```json
"User registered successfully"
```

### Register Student

**Request:**
```json
POST /api/auth/register
Content-Type: application/json

{
  "username": "jane_smith",
  "password": "SecurePass123!",
  "role": "STUDENT",
  "major": "Software Engineering"
}
```

### Login

**Request:**
```json
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePass123!"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTczMTUwNjQwMCwiZXhwIjoxNzMxNTkyODAwfQ...",
  "tokenType": "Bearer",
  "username": "john_doe",
  "role": "INSTRUCTOR"
}
```

### Create Course

**Request:**
```json
POST /api/courses
Authorization: Bearer {instructor_token}
Content-Type: application/json

{
  "name": "Introduction to Spring Boot",
  "description": "Learn Spring Boot from scratch with hands-on projects",
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
  "timestamp": "2025-11-13T10:30:00",
  "data": {
    "id": 1,
    "name": "Introduction to Spring Boot",
    "description": "Learn Spring Boot from scratch with hands-on projects",
    "price": 299.99,
    "duration": 40,
    "level": "BEGINNER",
    "instructorId": 1,
    "instructorName": "john_doe",
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
  "timestamp": "2025-11-13T10:35:00",
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
        "name": "Advanced Java Programming",
        "description": "Master advanced Java concepts",
        "price": 399.99,
        "duration": 60,
        "level": "ADVANCED",
        "instructorId": 1,
        "instructorName": "john_doe",
        "enrolledStudentsCount": 15
      }
    ]
  }
}
```

### Create Review

**Request:**
```json
POST /api/reviews
Authorization: Bearer {student_token}
Content-Type: application/json

{
  "courseId": 1,
  "rating": 5,
  "comment": "Excellent course! Very well structured and informative."
}
```

**Response:**
```json
{
  "success": true,
  "status": 200,
  "message": "Course reviewed Successfully",
  "timestamp": "2025-11-13T11:00:00",
  "data": {
    "id": 1,
    "rating": 5,
    "comment": "Excellent course! Very well structured and informative.",
    "studentId": 2,
    "studentName": "jane_smith",
    "courseId": 1,
    "courseName": "Introduction to Spring Boot",
    "createdAt": "2025-11-13T11:00:00",
    "updatedAt": "2025-11-13T11:00:00"
  }
}
```

### Instructor Dashboard

**Request:**
```
GET /api/instructors/dashboard
Authorization: Bearer {instructor_token}
```

**Response:**
```json
{
  "success": true,
  "status": 200,
  "message": "Dashboard data retrieved successfully",
  "timestamp": "2025-11-13T11:15:00",
  "data": {
    "totalCourses": 5,
    "totalStudents": 47,
    "totalReviews": 23,
    "averageRating": 4.6,
    "recentCourses": [
      {
        "id": 5,
        "name": "Microservices Architecture",
        "description": "Build scalable microservices",
        "price": 599.99,
        "duration": 80,
        "level": "ADVANCED",
        "instructorId": 1,
        "instructorName": "john_doe",
        "enrolledStudentsCount": 12
      }
    ],
    "recentReviews": [
      {
        "id": 23,
        "rating": 5,
        "comment": "Best course I've ever taken!",
        "studentId": 15,
        "studentName": "alice_wilson",
        "courseId": 5,
        "courseName": "Microservices Architecture",
        "createdAt": "2025-11-13T09:30:00",
        "updatedAt": "2025-11-13T09:30:00"
      }
    ]
  }
}
```

### Error Response Example

**Response:**
```json
{
  "success": false,
  "status": 404,
  "message": "Course not found with Id: '999'",
  "timestamp": "2025-11-13T12:00:00",
  "path": "/api/courses/999"
}
```

## 🔒 Security

### JWT Authentication Flow

```
1. User sends credentials → POST /api/auth/login
2. Server validates credentials
3. Server generates JWT token (valid for 24 hours)
4. Client stores token
5. Client includes token in Authorization header: "Bearer {token}"
6. Server validates token for each protected request
7. Server grants/denies access based on role
```

### Password Security
- All passwords encrypted using BCrypt
- Plain text passwords never stored in database
- Password strength validation on registration

### Role-Based Authorization
- Method-level security using `@PreAuthorize` annotation
- Instructors can only modify their own courses
- Students can only manage their own enrollments and reviews
- Public endpoints accessible without authentication

### Token Expiration
- Default: 24 hours (86400000 milliseconds)
- Configurable in `application.properties`
- Expired tokens rejected automatically

## 💾 Database Schema

### Entity Relationships

```
User (Abstract - Single Table Inheritance)
├── Instructor
│   ├── specialization: String
│   └── courses: List<Course> (One-to-Many)
│
└── Student
    ├── major: String
    ├── enrolledCourses: List<Course> (Many-to-Many)
    └── reviews: List<Review> (One-to-Many)

Course
├── instructor: Instructor (Many-to-One)
├── enrolledStudents: List<Student> (Many-to-Many)
└── reviews: List<Review> (One-to-Many)

Review
├── student: Student (Many-to-One)
├── course: Course (Many-to-One)
└── UNIQUE(student_id, course_id) - One review per student per course
```

### Database Tables

**user** - Single Table Inheritance
```sql
CREATE TABLE user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('INSTRUCTOR', 'STUDENT') NOT NULL,
  user_type VARCHAR(31) NOT NULL,  -- Discriminator
  specialization VARCHAR(255),     -- For Instructors
  major VARCHAR(255)                -- For Students
);
```

**course**
```sql
CREATE TABLE course (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(1000),
  price DOUBLE,
  duration INT,
  level VARCHAR(50),
  instructor_id BIGINT NOT NULL,
  FOREIGN KEY (instructor_id) REFERENCES user(id)
);
```

**review**
```sql
CREATE TABLE review (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
  comment VARCHAR(500),
  student_id BIGINT NOT NULL,
  course_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (student_id) REFERENCES user(id),
  FOREIGN KEY (course_id) REFERENCES course(id),
  UNIQUE KEY unique_student_course (student_id, course_id)
);
```

**student_course** - Join Table
```sql
CREATE TABLE student_course (
  student_id BIGINT NOT NULL,
  course_id BIGINT NOT NULL,
  PRIMARY KEY (student_id, course_id),
  FOREIGN KEY (student_id) REFERENCES user(id),
  FOREIGN KEY (course_id) REFERENCES course(id)
);
```

## 📦 Project Structure

```
school-management-rest-api/
├── src/
│   ├── main/
│   │   ├── java/com/springboot/school_management/
│   │   │   ├── SchoolManagementRestApiApplication.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CourseController.java
│   │   │   │   ├── InstructorController.java
│   │   │   │   ├── StudentController.java
│   │   │   │   └── ReviewController.java
│   │   │   │
│   │   │   ├── entity/
│   │   │   │   ├── User.java (Abstract)
│   │   │   │   ├── Instructor.java
│   │   │   │   ├── Student.java
│   │   │   │   ├── Course.java
│   │   │   │   ├── Review.java
│   │   │   │   └── Role.java (Enum)
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   │
│   │   │   ├── payload/
│   │   │   │   ├── CourseDto.java
│   │   │   │   ├── CourseRequest.java
│   │   │   │   ├── JwtAuthResponse.java
│   │   │   │   ├── LoginDto.java
│   │   │   │   ├── RegisterDto.java
│   │   │   │   ├── ReviewDto.java
│   │   │   │   ├── ReviewCreateRequest.java
│   │   │   │   ├── ReviewUpdateRequest.java
│   │   │   │   ├── instructor/
│   │   │   │   │   ├── InstructorDto.java
│   │   │   │   │   ├── InstructorProfileDto.java
│   │   │   │   │   ├── InstructorDashboardDto.java
│   │   │   │   │   ├── InstructorStatisticsDto.java
│   │   │   │   │   └── UpdateInstructorProfileRequest.java
│   │   │   │   └── student/
│   │   │   │       ├── StudentDto.java
│   │   │   │       ├── StudentProfileDto.java
│   │   │   │       ├── StudentDashboardDto.java
│   │   │   │       ├── StudentStatisticsDto.java
│   │   │   │       └── UpdateStudentProfileRequest.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── InstructorRepository.java
│   │   │   │   ├── StudentRepository.java
│   │   │   │   ├── CourseRepository.java
│   │   │   │   └── ReviewRepository.java
│   │   │   │
│   │   │   ├── response/
│   │   │   │   ├── ApiResponse.java
│   │   │   │   └── PageResponse.java
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── CourseService.java
│   │   │   │   ├── InstructorService.java
│   │   │   │   ├── StudentService.java
│   │   │   │   ├── ReviewService.java
│   │   │   │   └── impl/
│   │   │   │       ├── AuthServiceImpl.java
│   │   │   │       ├── CourseServiceImpl.java
│   │   │   │       ├── InstructorServiceImpl.java
│   │   │   │       ├── StudentServiceImpl.java
│   │   │   │       └── ReviewServiceImpl.java
│   │   │   │
│   │   │   └── utils/
│   │   │       ├── AppConstants.java
│   │   │       └── SecurityUtils.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/springboot/school_management/
│           └── SchoolManagementRestApiApplicationTests.java
│
├── pom.xml
└── README.md
```

## ⚙️ Configuration

### Application Properties

```properties
# Application Name
spring.application.name=school-management-rest-api

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/school
spring.datasource.username=root
spring.datasource.password=your_password

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Logging Configuration
logging.level.org.hibernate.SQL=debug
logging.level.org.hibernate.orm.jdbc.bind=trace

# JWT Configuration
app.jwt-secret=your_secret_key_minimum_256_bits
app-jwt-expiration-millisecond=86400000

# Server Configuration (Optional)
server.port=8080
server.servlet.context-path=/

# OpenAPI/Swagger Configuration
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Environment Variables (Production)

For production, use environment variables instead of hardcoding:

```bash
export DB_URL=jdbc:mysql://your-host:3306/school
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export JWT_SECRET=your_production_secret_key
export JWT_EXPIRATION=86400000
```

## 🧪 Testing

Run all tests:
```bash
mvn test
```

Run specific test:
```bash
mvn test -Dtest=CourseServiceTest
```

## 📊 API Documentation

Access interactive API documentation:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🚧 Common Issues & Solutions

### Issue: JWT Token Expired
**Solution**: Login again to get a new token. Token validity: 24 hours.

### Issue: Access Denied (403)
**Solution**: Ensure you're using the correct role token. Instructors cannot access student endpoints and vice versa.

### Issue: Course Not Found After Creation
**Solution**: Check if the instructor ID matches the token user. Only course owner can modify/delete courses.

### Issue: Already Reviewed This Course
**Solution**: Update existing review instead of creating a new one. One review per student per course.

### Issue: Must Be Enrolled to Review
**Solution**: Enroll in the course first before writing a review.

## 📈 Future Enhancements

- [ ] Admin role and dashboard
- [ ] Course categories and tags
- [ ] Advanced search and filtering
- [ ] File upload for course materials
- [ ] Payment integration
- [ ] Certificate generation upon completion
- [ ] Real-time notifications (WebSocket)
- [ ] Email notifications
- [ ] Analytics and reporting
- [ ] Multi-language support
- [ ] Docker containerization
- [ ] CI/CD pipeline with GitHub Actions
- [ ] Comprehensive unit and integration tests
- [ ] Redis caching for performance
- [ ] Rate limiting for API endpoints

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Contribution Guidelines
- Follow Java naming conventions
- Write meaningful commit messages
- Add unit tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Developer

**Omar Hamdi Ellafy**

- GitHub: [@OmarHamdi11](https://github.com/OmarHamdi11)
- LinkedIn: [Omar Hamdi](https://www.linkedin.com/in/omar-ellafy)
- Email: dev.omar.ellafy@gmail.com

## 🙏 Acknowledgments

- Spring Boot Documentation
- Spring Security Documentation
- JWT.io
- Hibernate ORM
- MySQL Documentation
- Stack Overflow Community

---

**Built with ❤️ using Spring Boot, Spring Security, JWT, and MySQL**

⭐ If you found this project helpful, please give it a star!