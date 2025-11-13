package com.springboot.school_management.service.impl;

import com.springboot.school_management.entity.Course;
import com.springboot.school_management.entity.Instructor;
import com.springboot.school_management.entity.Review;
import com.springboot.school_management.entity.Student;
import com.springboot.school_management.exception.ResourceNotFoundException;
import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.ReviewDto;
import com.springboot.school_management.payload.instructor.InstructorDto;
import com.springboot.school_management.payload.student.*;
import com.springboot.school_management.repository.CourseRepository;
import com.springboot.school_management.repository.ReviewRepository;
import com.springboot.school_management.repository.StudentRepository;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              CourseRepository courseRepository,
                              ReviewRepository reviewRepository,
                              ModelMapper modelMapper
    ) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageResponse<StudentDto> getAllStudents(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Student> studentsPage = studentRepository.findAll(pageable);

        Page<StudentDto> studentsDtoPage = studentsPage.map(this::mapToDto);

        return new PageResponse<>(studentsDtoPage);
    }

    @Override
    public StudentDto getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );

        return mapToDto(student);
    }

    @Override
    public StudentProfileDto getProfile(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );

        return mapToStudentProfile(student);
    }

    @Override
    public StudentProfileDto updateProfile(Long studentId, UpdateStudentProfileRequest request) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );

        student.setMajor(request.getMajor());
        Student savedStudent = studentRepository.save(student);

        return mapToStudentProfile(savedStudent);
    }

    @Override
    public StudentDashboardDto getDashboard(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );

        StudentDashboardDto dashboard = new StudentDashboardDto();

        List<Course> enrolledCourses = student.getEnrolledCourses();
        List<Review> reviews = student.getReviews();
        double averageRating = reviews.isEmpty()? 0.0 :
                reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);

        dashboard.setTotalEnrollments(enrolledCourses.size());
        dashboard.setTotalReviews(reviews.size());
        dashboard.setAverageRating(averageRating);

        List<CourseDto> courseDtoList = enrolledCourses.stream().map(this::mapToCourseDto).toList();
        dashboard.setEnrolledCourses(courseDtoList);

        List<ReviewDto> recentReviews = reviews.stream()
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                .limit(5)
                .map(this::mapToReviewDto)
                .toList();
        dashboard.setRecentReviews(recentReviews);

        return dashboard;
    }

    @Override
    public StudentStatisticsDto getStatistics(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", studentId)
        );
        StudentStatisticsDto statistics = new StudentStatisticsDto();
        List<Course> enrolledCourses = student.getEnrolledCourses();
        List<Review> reviews = student.getReviews();
        double averageRating = reviews.isEmpty()? 0.0 :
                reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);

        statistics.setTotalEnrollments(enrolledCourses.size());
        statistics.setTotalReviews(reviews.size());
        statistics.setAverageRating(averageRating);

        Map<Integer, Long> ratingDistribution = reviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));
        statistics.setGivenRatingDistribution(ratingDistribution);

        Map<String,Long> levelCount = enrolledCourses.stream()
                .collect(Collectors.groupingBy(Course::getLevel,Collectors.counting()));
        String mostEnrolledLevel = levelCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        statistics.setMostEnrolledLevel(mostEnrolledLevel);

        InstructorDto favouriteInstructor = enrolledCourses.stream()
                .collect(Collectors.groupingBy(Course::getInstructor,Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> mapToInstructorDto(entry.getKey()))
                .orElse(null);
        statistics.setFavouriteInstructor(favouriteInstructor);

        return statistics;
    }

    private StudentDto mapToDto(Student student){
        StudentDto studentDto = modelMapper.map(student,StudentDto.class);
        studentDto.setTotalEnrollments(student.getEnrolledCourses().size());

        List<Review> reviews = reviewRepository.findByStudentId(student.getId());
        studentDto.setTotalReviews(reviews.size());

        double averageRating = reviews.isEmpty()? 0.0 :
                reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);
        studentDto.setAverageRating(averageRating);

        return studentDto;
    }

    private StudentProfileDto mapToStudentProfile(Student student){
        return modelMapper.map(student, StudentProfileDto.class);
    }

    private CourseDto mapToCourseDto(Course course){
        CourseDto courseDto;

        courseDto = modelMapper.map(course, CourseDto.class);
        courseDto.setInstructorId(course.getInstructor().getId());
        courseDto.setInstructorName(course.getInstructor().getUsername());
        courseDto.setEnrolledStudentsCount(course.getEnrolledStudents().size());

        return courseDto;
    }

    private ReviewDto mapToReviewDto(Review review){
        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);

        reviewDto.setStudentId(review.getStudent().getId());
        reviewDto.setStudentName(review.getStudent().getUsername());
        reviewDto.setCourseId(review.getCourse().getId());
        reviewDto.setCourseName(review.getCourse().getName());

        return reviewDto;
    }

    private InstructorDto mapToInstructorDto(Instructor instructor){
        InstructorDto instructorDto;
        instructorDto = modelMapper.map(instructor, InstructorDto.class);

        List<Course> courses = courseRepository.findByInstructorId(instructor.getId());
        instructorDto.setTotalCourses(courses.size());

        int totalStudents = courses.stream().mapToInt(course -> course.getEnrolledStudents().size()).sum();
        instructorDto.setTotalStudents(totalStudents);

        List<Review> reviews = new ArrayList<>();
        for (Course course: courses){
            reviews.addAll(reviewRepository.findByCourseId(course.getId()));
        }
        double avgRating = reviews.isEmpty()? 0.0
                : reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        instructorDto.setAverageRating(Math.round(avgRating * 10.0) / 10.0);

        return instructorDto;
    }
}
