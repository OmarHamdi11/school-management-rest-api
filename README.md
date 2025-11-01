# School Management System

> ⚠️ **Project Status**: This project is currently under active development and is a work in progress.

## 📋 Overview

A comprehensive RESTful API for managing a school system built with Spring Boot. This application handles instructors, courses, students, and course reviews with complex relationships including one-to-one, one-to-many, and many-to-many associations using JPA/Hibernate.

## ✨ Features

- **Instructor Management**
  - Create, read, update, and delete instructors
  - Manage instructor details (YouTube channel, hobbies)
  - Link instructors to their courses
  - One-to-One relationship with InstructorDetail
  - One-to-Many relationship with Courses

- **Course Management**
  - Full CRUD operations for courses
  - Associate courses with instructors
  - Manage course reviews
  - Enroll students in courses
  - Many-to-Many relationship with Students
  - One-to-Many relationship with Reviews

- **Student Management**
  - Create, read, update, and delete students
  - Enroll students in multiple courses
  - Many-to-Many relationship with Courses

- **Review System**
  - Add reviews to courses
  - One-to-Many relationship (Course to Reviews)

- **API Documentation**
  - SpringDoc OpenAPI integration for interactive API documentation
  - Available at `/swagger-ui.html`

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.6
- **Java Version**: Java 21
- **ORM**: Spring Data JPA with Hibernate
- **Database**: MySQL
- **Build Tool**: Maven
- **API Documentation**: SpringDoc OpenAPI 2.8.9
- **Testing**: Spring Boot Test, Spring REST Docs

## 📦 Project Structure

```
school-management-jpa-hibernate/
├── src/main/java/com/ellafy/school_management_jpa_hibernate/
│   ├── SchoolManagementJpaHibernateApplication.java  # Main application
│   ├── dao/                          # Repository layer
│   │   ├── CourseRepository.java
│   │   ├── InstructorRepository.java
│   │   └── StudentRepository.java
│   ├── entity/                       # JPA Entities
│   │   ├── Course.java
│   │   ├── Instructor.java
│   │   ├── InstructorDetail.java
│   │   ├── Review.java
│   │   └── Student.java
│   ├── exception/                    # Custom exceptions
│   │   ├── NotFoundException.java
│   │   └── RestExceptionHandler.java
│   ├── response/                     # Response DTOs
│   │   ├── ApiResponse.java
│   │   └── ErrorResponse.java
│   ├── rest/                         # REST Controllers
│   │   ├── CourseRestController.java
│   │   ├── InstructorRestController.java
│   │   └── StudentRestController.java
│   └── service/                      # Business logic layer
│       ├── StudentService.java
│       ├── StudentServiceImpl.java
│       ├── courseService/
│       │   ├── CourseService.java
│       │   └── CourseServiceImpl.java
│       └── instructorService/
│           ├── InstructorService.java
│           └── InstructorServiceImpl.java
└── pom.xml
```

## 🗄️ Database Schema

The application uses a MySQL database with the following tables and relationships:

### Entity Relationships

```
Instructor (1) ←→ (1) InstructorDetail
Instructor (1) ←→ (Many) Course
Course (1) ←→ (Many) Review
Course (Many) ←→ (Many) Student [course_student join table]
```

### Tables

- **instructor**: Stores instructor information
- **instructor_detail**: Stores additional instructor details
- **course**: Stores course information
- **review**: Stores course reviews
- **student**: Stores student information
- **course_student**: Join table for many-to-many relationship

## 🔌 API Endpoints

### Instructors

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/instructors` | Create a new instructor |
| GET | `/api/instructors` | Get all instructors |
| GET | `/api/instructors/{instructorId}` | Get instructor by ID |
| DELETE | `/api/instructors/{instructorId}` | Delete instructor |

### Courses

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/courses` | Create a new course |
| GET | `/api/courses` | Get all courses |
| GET | `/api/courses/{courseId}` | Get course by ID |
| DELETE | `/api/courses/{courseId}` | Delete course |

### Students

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/students` | Create a new student |
| GET | `/api/students` | Get all students |
| GET | `/api/students/{studentId}` | Get student by ID |
| DELETE | `/api/students/{studentId}` | Delete student |

## 📊 Request/Response Examples

### Create Instructor
**POST** `/api/instructors`
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "instructorDetail": {
    "youtubeChannel": "youtube.com/johndoe",
    "hobby": "Coding"
  }
}
```

**Response:**
```json
{
  "status": 200,
  "message": "Success",
  "timeStamp": 1640000000000,
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "instructorDetail": {
      "id": 1,
      "youtubeChannel": "youtube.com/johndoe",
      "hobby": "Coding"
    }
  }
}
```

### Create Course
**POST** `/api/courses`
```json
{
  "title": "Spring Boot Masterclass",
  "instructor": {
    "id": 1
  }
}
```

### Create Student
**POST** `/api/students`
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com"
}
```

### Error Response
```json
{
  "status": 404,
  "message": "Student with id - 999 Not Found",
  "timeStamp": 1640000000000
}
```

## ⚙️ Configuration

Update `src/main/resources/application.properties` with your database configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/school_management
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Logging configuration
logging.level.root=warn
logging.level.org.hibernate.SQL=trace
logging.level.org.hibernate.orm.jdbc.bind=trace
```

## 🚀 Getting Started

### Prerequisites

- **JDK 21** or higher
- **Maven 3.6+**
- **MySQL Server** (running on localhost:3306)
- A MySQL database named `school_management`

### Database Setup

1. Create the MySQL database:
```sql
CREATE DATABASE school_management;
```

2. The application will automatically create the required tables on first run (DDL auto-update is enabled)

### Installation & Running

1. **Clone the repository:**
```bash
git clone <repository-url>
cd school-management-jpa-hibernate
```

2. **Configure the database** in `src/main/resources/application.properties`

3. **Build the project:**
```bash
mvn clean install
```

4. **Run the application:**
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### Access API Documentation

Once the application is running, access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

## 🏗️ Architecture

The application follows a **layered architecture pattern**:

1. **Controller Layer** (REST Controllers)
   - Handles HTTP requests and responses
   - Validates input data
   - Returns standardized API responses

2. **Service Layer** (Business Logic)
   - Contains business rules and logic
   - Coordinates between controllers and repositories
   - Handles transaction management

3. **Repository Layer** (Data Access)
   - Manages database operations
   - Uses Spring Data JPA for automatic CRUD operations

4. **Entity Layer**
   - Represents database tables as Java objects
   - Defines relationships between entities

## 🔑 Key Implementation Details

### Cascade Types
The application uses selective cascade operations to maintain data integrity:
- **Instructor → InstructorDetail**: CascadeType.ALL
- **Instructor → Courses**: DETACH, MERGE, PERSIST, REFRESH (excludes REMOVE)
- **Course → Students**: DETACH, MERGE, PERSIST, REFRESH
- **Course → Reviews**: CascadeType.ALL

### Fetch Strategies
- **Lazy Loading**: Used for collections (courses, students, reviews) to optimize performance
- Prevents N+1 query problems

### Exception Handling
- Global exception handler using `@ControllerAdvice`
- Custom `NotFoundException` for 404 errors
- Generic exception handler for other errors
- Consistent error response format

### Bidirectional Relationships
- **Instructor ↔ InstructorDetail**: Bidirectional one-to-one
- **Instructor ↔ Course**: Bidirectional one-to-many
- **Course ↔ Student**: Bidirectional many-to-many

### Helper Methods
Convenience methods for managing relationships:
- `Instructor.add(Course)`: Adds course and sets instructor reference
- `Course.addReview(Review)`: Adds review to course
- `Course.addStudent(Student)`: Enrolls student in course

## 🧪 Testing

Run tests using:
```bash
mvn test
```

## 📝 API Response Structure

### Success Response
```json
{
  "status": 200,
  "message": "Success",
  "timeStamp": 1640000000000,
  "data": { }
}
```

### Error Response
```json
{
  "status": 404,
  "message": "Error description",
  "timeStamp": 1640000000000
}
```

## 🔧 Configuration Properties

| Property | Value | Description |
|----------|-------|-------------|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/school_management` | Database URL |
| `spring.jpa.hibernate.ddl-auto` | `update` | Auto schema generation |
| `logging.level.org.hibernate.SQL` | `trace` | SQL query logging |
| `logging.level.org.hibernate.orm.jdbc.bind` | `trace` | Parameter binding logging |

## 🔮 Future Enhancements

- [ ] Update operations (PUT endpoints)
- [ ] Input validation with Bean Validation
- [ ] Authentication and authorization (Spring Security)
- [ ] JWT token-based authentication
- [ ] Pagination and sorting for list endpoints
- [ ] Search and filter functionality
- [ ] File upload for instructor/student profiles
- [ ] Grade management system
- [ ] Attendance tracking
- [ ] Assignment and submission management
- [ ] Email notifications
- [ ] Department management
- [ ] Class schedule management
- [ ] Report generation
- [ ] Advanced analytics dashboard
- [ ] Integration tests
- [ ] Docker containerization
- [ ] CI/CD pipeline

## 🐛 Troubleshooting

**Database Connection Issues:**
- Ensure MySQL is running on localhost:3306
- Verify database credentials in `application.properties`
- Check if the `school_management` database exists

**Port Already in Use:**
- Change the server port in `application.properties`:
  ```properties
  server.port=8081
  ```

**Hibernate DDL Issues:**
- If you encounter schema issues, try setting:
  ```properties
  spring.jpa.hibernate.ddl-auto=create
  ```
  (Warning: This will drop existing tables)

## 📚 Dependencies

Key dependencies from `pom.xml`:
- `spring-boot-starter-data-jpa` - JPA and Hibernate
- `spring-boot-starter-web` - Spring MVC and REST
- `mysql-connector-j` - MySQL driver
- `springdoc-openapi-starter-webmvc-ui` - API documentation
- `spring-restdocs-mockmvc` - REST documentation
- `spring-boot-starter-test` - Testing framework

## 🔒 Security Note

The database credentials are currently hardcoded in `application.properties`. For production:
1. Use environment variables
2. Use Spring Boot profiles
3. Implement Spring Vault or similar secret management
4. Add `application.properties` to `.gitignore`

## 👨‍💻 Author

Omar Hamdi Ellafy

## 📄 License

This project is licensed under the MIT License.

---

**Built with ❤️ using Spring Boot, JPA/Hibernate, and MySQL**
