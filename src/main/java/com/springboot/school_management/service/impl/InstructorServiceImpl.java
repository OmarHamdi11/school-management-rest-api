package com.springboot.school_management.service.impl;

import com.springboot.school_management.entity.Course;
import com.springboot.school_management.entity.Instructor;
import com.springboot.school_management.entity.Review;
import com.springboot.school_management.exception.ResourceNotFoundException;
import com.springboot.school_management.payload.CourseDto;
import com.springboot.school_management.payload.ReviewDto;
import com.springboot.school_management.payload.instructor.*;
import com.springboot.school_management.repository.CourseRepository;
import com.springboot.school_management.repository.InstructorRepository;
import com.springboot.school_management.repository.ReviewRepository;
import com.springboot.school_management.response.PageResponse;
import com.springboot.school_management.service.InstructorService;
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
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository, CourseRepository courseRepository, ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageResponse<InstructorDto> getAllInstructors(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Instructor> instructorPage = instructorRepository.findAll(pageable);

        Page<InstructorDto> instructorDtoPage = instructorPage.map(this::mapToDto);

        return new PageResponse<>(instructorDtoPage);
    }

    @Override
    public InstructorDto getInstructorById(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor", "id", instructorId)
        );

        return mapToDto(instructor);
    }

    @Override
    public List<CourseDto> getInstructorCourses(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor", "id", instructorId)
        );

        List<Course> courses = courseRepository.findByInstructorId(instructor.getId());

        return courses.stream().map(this::mapToCourseDto).toList();
    }

    @Override
    public List<InstructorDto> searchInstructors(String specialization) {
        List<Instructor> instructors = instructorRepository.findBySpecializationContainingIgnoreCase(specialization);

        return instructors.stream().map(this::mapToDto).toList();
    }

    @Override
    public InstructorProfileDto getProfile(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor","id",instructorId)
        );

        return mapToInstructorProfile(instructor);
    }

    @Override
    public InstructorProfileDto updateProfile(Long instructorId, UpdateInstructorProfileRequest request) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor","id",instructorId)
        );

        instructor.setSpecialization(request.getSpecialization());
        Instructor savedInstructor = instructorRepository.save(instructor);

        return mapToInstructorProfile(instructor);
    }

    @Override
    public InstructorDashboardDto getDashboard(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor","id",instructorId)
        );

        InstructorDashboardDto dashboard = new InstructorDashboardDto();

        List<Course> courses = courseRepository.findByInstructorId(instructor.getId());
        List<Review> allReviews = new ArrayList<>();
        int totalStudents = courses.stream()
                .mapToInt(course -> course.getEnrolledStudents().size())
                .sum();

        for (Course course: courses){
            allReviews.addAll(reviewRepository.findByCourseId(course.getId()));
        }
        double avgRating = allReviews.isEmpty()? 0.0
                : allReviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

        dashboard.setTotalCourses(courses.size());
        dashboard.setTotalStudents(totalStudents);
        dashboard.setTotalReviews(allReviews.size());
        dashboard.setAverageRating(avgRating);

        List<CourseDto> recentCourses = courses.stream()
                .sorted(Comparator.comparing(Course::getId).reversed())
                .limit(5)
                .map(this::mapToCourseDto)
                .toList();
        dashboard.setRecentCourses(recentCourses);

        List<ReviewDto> recentReviews = allReviews.stream()
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                .limit(5)
                .map(this::mapToReviewDto)
                .toList();
        dashboard.setRecentReviews(recentReviews);

        return dashboard;
    }

    @Override
    public InstructorStatisticsDto getStatistics(Long instructorId) {
        if (!instructorRepository.existsById(instructorId)){
            throw new ResourceNotFoundException("Instructor","id",instructorId);
        }

        InstructorStatisticsDto statistics = new InstructorStatisticsDto();

        List<Course> courses = courseRepository.findByInstructorId(instructorId);
        List<Review> allReviews = new ArrayList<>();
        int totalStudents = courses.stream()
                .mapToInt(course -> course.getEnrolledStudents().size())
                .sum();

        for (Course course: courses){
            allReviews.addAll(reviewRepository.findByCourseId(course.getId()));
        }
        double avgRating = allReviews.isEmpty()? 0.0
                : allReviews.stream().mapToInt(Review::getRating).average().orElse(0.0);


        // Rating Distribution
        Map<Integer, Long> ratingDistribution = allReviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

        // Most Popular Course (الأكثر طلاباً)
        CourseDto mostPopularCourse = courses.stream()
                .max(Comparator.comparing(c -> c.getEnrolledStudents().size()))
                .map(this::mapToCourseDto)
                .orElse(null);

        // Highest Rated Course
        CourseDto highestRatedCourse = null;
        double highestRating = 0.0;
        for (Course course : courses) {
            Double courseRating = reviewRepository.findByAverageRatingByCourseId(course.getId());
            if (courseRating != null && courseRating > highestRating) {
                highestRating = courseRating;
                highestRatedCourse = mapToCourseDto(course);
            }
        }

        statistics.setTotalCourses(courses.size());
        statistics.setTotalStudents(totalStudents);
        statistics.setTotalReviews(allReviews.size());
        statistics.setAverageRating(avgRating);
        statistics.setRatingDistribution(ratingDistribution);
        statistics.setHighestRatedCourse(highestRatedCourse);
        statistics.setMostPopularCourse(mostPopularCourse);

        return statistics;
    }

    private InstructorDto mapToDto(Instructor instructor){
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


    private CourseDto mapToCourseDto(Course course){
        CourseDto courseDto;

        courseDto = modelMapper.map(course, CourseDto.class);
        courseDto.setInstructorId(course.getInstructor().getId());
        courseDto.setInstructorName(course.getInstructor().getUsername());
        courseDto.setEnrolledStudentsCount(course.getEnrolledStudents().size());

        return courseDto;
    }

    private InstructorProfileDto mapToInstructorProfile(Instructor instructor){
        InstructorProfileDto instructorProfileDto;
        instructorProfileDto = modelMapper.map(instructor,InstructorProfileDto.class);
        return instructorProfileDto;
    }

    private ReviewDto mapToReviewDto(Review review){
        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);

        reviewDto.setStudentId(review.getStudent().getId());
        reviewDto.setStudentName(review.getStudent().getUsername());
        reviewDto.setCourseId(review.getCourse().getId());
        reviewDto.setCourseName(review.getCourse().getName());

        return reviewDto;
    }
}
